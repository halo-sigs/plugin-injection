import type { Metadata } from "@halo-dev/api-client"
export interface HtmlInjectionSpec {
  name: string;
  description: string;
  fragment: string;
  injectionPoint: 'HEADER' | 'FOOTER';
  pageRules:string[];
  enabled: boolean;
}

/**
 * 与自定义模型 HtmlInjection 对应
 */
export interface HtmlInjection {
  spec: HtmlInjectionSpec;
  apiVersion: "theme.halo.run/v1alpha1"; // apiVersion=自定义模型的 group/version
  kind: "HtmlInjection"; // Code 自定义模型中 @GVK 注解中的 kind
  metadata: Metadata;
}

/**
 * HtmlInjection 自定义模型生成 list API 所对应的类型
 */
export interface HtmlInjectionList {
  page: number;
  size: number;
  total: number;
  items: Array<HtmlInjection>;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
  totalPages: number;
}
