<script setup lang="ts">
import { ref, watch } from 'vue';
import {
  VCard,
  VButton,
  VModal,
  VSpace,
  VTabbar
} from '@halo-dev/components';
import type { HtmlInjection } from "@/types";
import { cloneDeep } from 'lodash-es';
import { axiosInstance } from "@halo-dev/api-client";

// 接收 props
const props = defineProps<{
  /**
   * 当前编辑的 HTML 注入对象，如果为空就是新增注入的逻辑，不为空就是修改选择的注入
   * @type {HtmlInjection | null}
   */
  htmlInjection: HtmlInjection | null,
}>();


// 定义 emit 事件
const emit = defineEmits(['close', 'submit']);

// 初始化表单数据
const initialFormData = {
  name: '',
  description: '',
  fragment: '',
  injectionPoint: 'HEADER' as 'HEADER' | 'FOOTER',
  pageRules: [] as string[],
  isEnabled: false,
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
      isEnabled: currentHtmlInjection.spec.enabled,
    };
  }
};

// 监听 props.htmlInjection 的变化并相应更新 formData
watch(
  () => props.htmlInjection,
  updateFormData,
  { immediate: true }
);

// 关闭模态窗口
const closeModal = () => {
  emit('close');
};

// 提交表单
const submitForm = () => {
  const requestData: HtmlInjection= {
    apiVersion: 'theme.halo.run/v1alpha1',
    kind: 'HtmlInjection',
    metadata: {
      name: props.htmlInjection ? props.htmlInjection.metadata.name : '',
      generateName: 'htmlinjection-', // 使用现有注入的名称
    },
    spec: {
      name: formData.value.name,
      description: formData.value.description,
      fragment: formData.value.fragment,
      injectionPoint: formData.value.injectionPoint,
      pageRules: formData.value.pageRules, 
      enabled: formData.value.isEnabled,
    },
  };
  
  
  const url = props.htmlInjection
    ? `/apis/theme.halo.run/v1alpha1/htmlinjections/${props.htmlInjection.metadata.name}`
    : '/apis/theme.halo.run/v1alpha1/htmlinjections';
  const method = props.htmlInjection ? 'put' : 'post';
  const data = method === 'put' ? { ...props.htmlInjection, spec: requestData.spec } : requestData;
  axiosInstance({
    method,
    url,
    data
  })
    .then(() => {
      emit('submit', formData.value);
      closeModal();
    })
    .catch((error) => {
      console.error('Error creating or updating html injection:', error);
    });
};

// 选项卡数据
const tabs = ref([
  { id: "form", label: '基本信息'},
  { id: "content", label: '代码内容' }
]);

// 当前激活的选项卡
const activeTab = ref("form");

</script>

<template>
  <VModal
    :title="'新增注入'"
    :width="700"
    @close="closeModal()"
  >
    <div class="mt-5 divide-y divide-gray-100 md:col-span-3 md:mt-0">
      <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4">
        <template #header>
          <VTabbar
            v-model:active-id="activeTab"
            :items="tabs.map((item) => ({ id: item.id, label: item.label }))"
            class="w-full !rounded-none"
            type="outline"
          ></VTabbar>
        </template>
      </VCard>
    </div>

    <div class="mt-5 divide-y divide-gray-100 md:col-span-3 md:mt-0" v-show="activeTab === 'form'">
      <FormKit
        type="form"
        id="html-injection-form"
        name="html-injection-form"
        @submit="submitForm"
        :config="{ validationVisibility: 'submit' }"
        :actions="false"
      >
            <FormKit
              id="name"
              name="name"
              :label="'名称'"
              type="text"
              v-model="formData.name"
              validation="required|length:0,100"
              :placeholder="'请输入名称'"
            />
            <FormKit
              id="description"
              name="description"
              :label="'代码描述'"
              type="textarea"
              v-model="formData.description"
              validation="length:0,500"
              :placeholder="'请输入代码描述'"
            />
            <FormKit
              id="injectionPoint"
              name="injectionPoint"
              :label="'注入点'"
              type="select"
              v-model="formData.injectionPoint"
              :options="[
                { value: 'HEADER', label: 'Header' },
                { value: 'FOOTER', label: 'Footer' }
              ]"
            />
            <FormKit
              v-model="formData.pageRules"
              id="pageRules"
              name="pageRules"
              :label="'页面匹配规则'"
              type="list"
              item-type="string"
              add-label="添加"
            >
              <template #default="{ index }">
                <FormKit
                  type="text"
                  :index="index"
                  help="用于匹配页面路径的正则表达式，如：/archives/**"
                />
              </template>
            </FormKit>
            <FormKit
              id="isEnabled"
              name="isEnabled"
              :label="'启用'"
              type="checkbox"
              v-model="formData.isEnabled"
            />
      </FormKit>
    </div>

    <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4" v-show="activeTab === 'content'">
      <VCodemirror v-model="formData.fragment" height="500px" language="html" />
    </VCard>

    <template #footer>
      <VSpace>
        <VButton type="secondary" @click="submitForm">
          提交
        </VButton>
        <VButton @click="closeModal">
          取消
        </VButton>
      </VSpace>
    </template>
  </VModal>
</template>

<style>
@import "@halo-dev/components/dist/style.css";
</style>
