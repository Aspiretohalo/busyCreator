import { ElMessage } from "element-plus";

const GenerateIdeas = (ideaForm: {
    type: string,
    theme: string,
    description: string,
    genre: string,
    content: string
}): EventSource => {
    const token = localStorage.getItem('token');
    const isDev = process.env.NODE_ENV === 'development';
    const baseUrl = isDev ? 'http://localhost:8101' : '';

    // 创建 EventSource 实例
    const eventSource = new EventSourcePolyfill(
        `${baseUrl}/api/ai/idea/create`,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Accept': 'text/event-stream',
            },
            method: 'POST',
            body: JSON.stringify({
                type: ideaForm.type,
                theme: ideaForm.theme,
                description: ideaForm.description,
                genre: ideaForm.genre,
                content: ideaForm.content
            }),
            withCredentials: true
        }
    );

    // 添加默认的错误处理
    eventSource.onerror = (error) => {
        console.error('生成创意失败:', error);
        ElMessage({
            showClose: true,
            message: '生成创意失败',
            type: 'error',
        });
        eventSource.close();
    };

    return eventSource;
};

// EventSource 的 Polyfill，支持 POST 请求
class EventSourcePolyfill extends EventTarget {
    private url: string;
    private options: RequestInit;
    private abortController: AbortController;
    private buffer: string = '';
    private _onmessage: ((event: MessageEvent) => void) | null = null;

    constructor(url: string, options: RequestInit = {}) {
        super();
        this.url = url;
        this.options = options;
        this.abortController = new AbortController();
        this.connect();
    }

    private async connect() {
        try {
            const response = await fetch(this.url, {
                ...this.options,
                signal: this.abortController.signal
            });

            if (!response.ok) {
                const errorText = await response.text();
                throw new Error(`HTTP error! status: ${response.status}, body: ${errorText}`);
            }

            const reader = response.body?.getReader();
            if (!reader) throw new Error('No reader available');

            const decoder = new TextDecoder();

            while (true) {
                const { done, value } = await reader.read();

                if (done) {
                    console.log('数据流结束');
                    break;
                }

                const chunk = decoder.decode(value, { stream: true });
                this.buffer += chunk;

                // 按行分割处理数据
                const lines = this.buffer.split('\n');

                // 处理完整的行
                for (let i = 0; i < lines.length - 1; i++) {
                    const line = lines[i].trim();
                    if (!line) continue;

                    if (line.startsWith('data:')) {
                        const data = line.slice(5).trim();

                        if (data === '[DONE]') {
                            this.close();
                            return;
                        }

                        // 发送消息事件
                        if (data && this._onmessage) {
                            const messageEvent = new MessageEvent('message', {
                                data: data
                            });
                            this._onmessage(messageEvent);
                        }
                    }
                }

                // 保存未处理完的最后一行
                this.buffer = lines[lines.length - 1];
            }
        } catch (error) {
            console.error('SSE连接错误:', error);
            const errorEvent = new Event('error');
            this.dispatchEvent(errorEvent);
            throw error;
        }
    }

    set onmessage(handler: ((event: MessageEvent) => void) | null) {
        this._onmessage = handler;
    }

    get onmessage(): ((event: MessageEvent) => void) | null {
        return this._onmessage;
    }

    addEventListener(type: string, listener: EventListener) {
        super.addEventListener(type, listener);
    }

    removeEventListener(type: string, listener: EventListener) {
        super.removeEventListener(type, listener);
    }

    close() {
        this.abortController.abort();
    }
}

export default GenerateIdeas;