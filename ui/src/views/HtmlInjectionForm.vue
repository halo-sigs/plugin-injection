<script setup lang="ts">
import {ref, watch} from 'vue';
import type {HtmlInjection, HtmlInjectionSpec} from "@/types";
import {cloneDeep} from "lodash-es";

const props = defineProps<{
  htmlInjection: HtmlInjection | null;
}>();

const emit = defineEmits<{
  (event: "submit", data: HtmlInjectionSpec): void;
}>();

// 初始化表单数据
const initialFormData = {
  name: '',
  description: '',
  fragment: '',
  injectionPoint: 'HEADER' as 'HEADER' | 'FOOTER',
  pageRules: [] as string[],
  enabled: false,
};

const formData = ref(cloneDeep(initialFormData));

// 更新表单数据
const updateFormData = (currentHtmlInjection: HtmlInjection | null) => {
  if (currentHtmlInjection) {
    formData.value = {
      name: currentHtmlInjection.spec.name,
      description: currentHtmlInjection.spec.description,
      fragment: currentHtmlInjection.spec.fragment,
      injectionPoint: currentHtmlInjection.spec.injectionPoint,
      pageRules: currentHtmlInjection.spec.pageRules,
      enabled: currentHtmlInjection.spec.enabled,
    };
  }
};

// 监听 props.htmlInjection 的变化并相应更新 formData
watch(
  () => props.htmlInjection,
  updateFormData,
  {immediate: true}
);

// 提交表单
function onSubmit(formData: HtmlInjectionSpec) {
  // 触发 submit 事件并传递表单数据
  emit('submit', formData);
}
</script>

<template>
  <FormKit
    type="form"
    id="html-injection-form"
    name="html-injection-form"
    v-model="formData"
    :config="{ validationVisibility: 'submit'}"
    :actions="false"
    @submit="onSubmit"
  >
    <FormKit id="name" name="name" v-model="formData.name" :label="'名称'" type="text"
             validation="required|length:1,100" :placeholder="'请输入名称'"/>
    <FormKit id="description" name="description" v-model="formData.description" :label="'代码描述'" type="textarea"
             validation="length:0,500" :placeholder="'请输入代码描述'"/>
    <FormKit id="injectionPoint" name="injectionPoint" v-model="formData.injectionPoint" :label="'注入点'" type="select"
             :options="[
        { value: 'HEADER', label: 'Header' },
        { value: 'FOOTER', label: 'Footer' }
      ]"
    />
    <FormKit id="pageRules" name="pageRules" v-model="formData.pageRules" :label="'页面匹配规则'" validation="required"
             type="list" item-type="string" add-label="添加">
      <template #default="{ index }">
        <FormKit type="text" :index="index" help="用于匹配页面路径的符合 Ant-style 的表达式，如：/archives/**"/>
      </template>
    </FormKit>
    <FormKit id="enabled" name="enabled" v-model="formData.enabled" :label="'启用'" type="checkbox"/>
    <FormKit id="fragment" name="fragment" v-model="formData.fragment" :label="'代码'" type="code" :language="'html'"
             :height="'200px'"/>
  </FormKit>
</template>

<style>
@import "@halo-dev/components/dist/style.css";
</style>
