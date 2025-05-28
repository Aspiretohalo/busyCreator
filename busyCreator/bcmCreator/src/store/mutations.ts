export default {
    setDrawRecord(state: any, newDrawRecord: any) {
        state.drawRecord = newDrawRecord;
    },

    setMessage(state: any, newMessage: string) {
        state.message = newMessage;
    },

    clearMessage(state: any) {
        state.message = '';
    },
    setAiMessageType(state: any, newMessage: string) {
        state.aiMessageType = newMessage;
    },

    clearAiMessageType(state: any) {
        state.aiMessageType = '';
    },
    setIdeasForm(state: any, newMessage: object) {
        state.ideasForm = newMessage;
    },
    clearIdeasForm(state: any) {
        state.ideasForm = {
            type: '',
            theme: '',
            description: '',
            genre: '',
            content: ''
        };
    },
    // 更新灵感数据
    setInspirationData(state: any, data: any) {
        state.inspirationData = data;
    },
}