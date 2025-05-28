/**
 * 将 ISO 8601 格式的时间字符串转换为自定义格式
 * @param isoDate - ISO 8601 格式的时间字符串
 * @returns 格式化后的时间字符串
 */
export function formatDateTime(isoDate: string): string {
    // 创建 Date 对象
    const date = new Date(isoDate);

    // 获取年、月、日、时、分、秒
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从 0 开始
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    // 返回格式化后的字符串
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

export function formatDate(isoDate: string): string {
    // 创建 Date 对象
    const date = new Date(isoDate);

    // 获取年、月、日、时、分、秒
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从 0 开始
    const day = String(date.getDate()).padStart(2, '0');

    // 返回格式化后的字符串
    return `${year}-${month}-${day}`;
}
