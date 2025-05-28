const sendQuestion = (sessionId: number, question: string, context: string, type: string): EventSource => {
    const token = localStorage.getItem('token');
    const isDev = process.env.NODE_ENV === 'development';
    const baseUrl = isDev ? 'http://localhost:8101' : '';

    // 创建 EventSource 实例
    const eventSource = new EventSourcePolyfill(
        `${baseUrl}/api/ai/ask`,
        {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
                'Accept': 'text/event-stream',
            },
            method: 'POST',
            body: JSON.stringify({
                sessionId,
                question,
                context,
                type
            }),
            withCredentials: true
        }
    );

    eventSource.onmessage = (event) => {
        if (!event.data) return;
        let newContent = event.data;

        // 保证 markdown 换行
        // 如果后端已经返回了 markdown 格式，这里不用处理
        // 如果后端只返回了普通文本，可以这样处理
        // newContent = newContent.replace(/\\n/g, '\\n\\n'); // 让单换行变成段落

        streamingContent.value += newContent;
        nextTick(() => {
            scrollToBottom();
        });
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
            console.log('开始读取数据流...');

            while (true) {
                const { done, value } = await reader.read();

                if (done) {
                    console.log('数据流结束');
                    break;
                }

                const chunk = decoder.decode(value, { stream: true });
                console.log('收到原始数据块:', chunk);
                this.buffer += chunk;

                // 尝试按行分割
                const lines = this.buffer.split('\n');
                console.log('分割后的行数:', lines.length);

                // 处理完整的行
                for (let i = 0; i < lines.length - 1; i++) {
                    const line = lines[i].trim();
                    if (!line) continue; // 跳过空行

                    if (line.startsWith('data:')) {
                        const data = line.slice(5).trim();
                        console.log('提取的数据:', data);

                        if (data === '[DONE]') {
                            console.log('收到结束信号');
                            this.close();
                            return;
                        }

                        // 创建并发送消息事件
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

    // getter 和 setter 用于访问私有属性
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

export default sendQuestion;