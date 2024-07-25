
import dayjs from "dayjs";
import timezone from "dayjs/plugin/timezone";
import utc from "dayjs/plugin/utc";

// 初始化 Day.js 插件
dayjs.extend(timezone);
dayjs.extend(utc);

/**
 * 格式化日期时间
 * @param date - 日期字符串或 Date 对象
 * @param tz - 时区
 * @returns 格式化后的日期字符串
 */
export const formatDatetime = (date: string | Date | undefined | null, tz?: string): string => {
  return date ? dayjs(date).tz(tz).format("YYYY-MM-DD HH:mm") : "";
};
