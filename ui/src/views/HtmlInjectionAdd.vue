<script setup lang="ts">
import { ref, watch } from 'vue';
import {
  VCard,
  VButton,
  VModal,
  VSpace,
  VTabbar
} from '@halo-dev/components';
import axios from "axios";
import type { HtmlInjection } from "@/types";
import { cloneDeep } from 'lodash-es';
import { useI18n } from "vue-i18n";


const { t } = useI18n();

// 接收 props
const props = defineProps<{
  /**
   * 控制模态窗口的显示状态
   * @type {boolean}
   * @default false
   */
  visible: boolean;

  /**
   * 当前编辑的 HTML 注入对象，如果为空就是新增注入的逻辑
   * @type {HtmlInjection | null}
   */
  htmlInjection: HtmlInjection | null;
}>();


// 定义 emit 事件
const emit = defineEmits(['update:visible', 'close', 'submit']);

// 创建 Axios 实例
const http = axios.create({
  baseURL: "/",
  timeout: 1000,
});

// 初始化表单数据
const initialFormData = {
  name: '',
  description: '',
  fragment: '',
  injectionPoint: 'HEADER',
  pageRules: '',
  isEnabled: false,
};

const formData = ref(cloneDeep(initialFormData));

// 重置表单数据
const resetFormData = () => {
  formData.value = cloneDeep(initialFormData);
};

// 更新表单数据
const updateFormData = (currentHtmlInjection: HtmlInjection | null) => {
  if (currentHtmlInjection) {
    formData.value = {
      name: currentHtmlInjection.spec.name,
      description: currentHtmlInjection.spec.description,
      fragment: currentHtmlInjection.spec.fragment,
      injectionPoint: currentHtmlInjection.spec.injectionPoint,
      pageRules: Array.from(currentHtmlInjection.spec.pageRules).join(', '),
      isEnabled: currentHtmlInjection.spec.enabled,
    };
  } else {
    resetFormData();
  }
};

// 监听 props.htmlInjection 的变化并相应更新 formData
watch(
  () => props.htmlInjection,
  updateFormData,
  { immediate: true }
);

// 监听 props.visible 的变化以在关闭模态窗口时重置表单
watch(
  () => props.visible,
  (visible) => {
    if (!visible) {
      resetFormData();
    }
  }
);

// 关闭模态窗口
const closeModal = () => {
  emit('update:visible', false);
  emit('close');
};

// 提交表单
const submitForm = () => {
  const requestData = {
    apiVersion: 'theme.halo.run/v1alpha1',
    kind: 'HtmlInjection',
    metadata: {
      generateName: 'htmlinjection-', // 使用现有注入的名称
    },
    spec: {
      name: formData.value.name,
      description: formData.value.description,
      fragment: formData.value.fragment,
      injectionPoint: formData.value.injectionPoint,
      pageRules: Array.from(formData.value.pageRules.split(',').map((page) => page.trim())), // 转换为数组
      enabled: formData.value.isEnabled,
    },
  };

  //console.log('Request Data:', requestData);

  const url = props.htmlInjection
    ? `/apis/theme.halo.run/v1alpha1/htmlinjections/${props.htmlInjection.metadata.name}`
    : '/apis/theme.halo.run/v1alpha1/htmlinjections';
  const method = props.htmlInjection ? 'put' : 'post';
  const data = method === 'put' ? { ...props.htmlInjection, spec: requestData.spec } : requestData;
  http({
    method,
    url,
    data: data
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
  { id: "form", label: t("基本信息")},
  { id: "content", label: t("代码内容") }
]);

// 当前激活的选项卡
const activeTab = ref("form");

</script>

<template>
  <VModal
    :visible="visible"
    :title="t('新增注入')"
    :width="700"
    @update:visible="closeModal"
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
        <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4">
          <div class="p-4">
            <FormKit
              id="name"
              name="name"
              :label="t('名称')"
              type="text"
              v-model="formData.name"
              validation="required|length:0,100"
              :placeholder="t('请输入名称')"
            />
          </div>
        </VCard>

        <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4">
          <div class="p-4">
            <FormKit
              id="description"
              name="description"
              :label="t('代码描述')"
              type="textarea"
              v-model="formData.description"
              validation="length:0,500"
              :placeholder="t('请输入代码描述')"
            />
          </div>
        </VCard>

        <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4">
          <div class="p-4">
            <FormKit
              id="injectionPoint"
              name="injectionPoint"
              :label="t('注入点')"
              type="select"
              v-model="formData.injectionPoint"
              :options="[
                { value: 'HEADER', label: 'Header' },
                { value: 'FOOTER', label: 'Footer' }
              ]"
            />
          </div>
        </VCard>

        <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4">
          <div class="p-4">
            <FormKit
              id="pageRules"
              name="pageRules"
              :label="t('匹配规则')"
              type="text"
              v-model="formData.pageRules"
              :placeholder="t('请输入要注入的路径')"
            />
          </div>
        </VCard>

        <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4">
          <div class="p-4">
            <FormKit
              id="isEnabled"
              name="isEnabled"
              :label="t('启用')"
              type="checkbox"
              v-model="formData.isEnabled"
            />
          </div>
        </VCard>
      </FormKit>
    </div>

    <VCard :body-class="['!p-0', '!overflow-visible']" class="mb-4" v-show="activeTab === 'content'">
      <VCodemirror v-model="formData.fragment" height="500px" language="html" />
    </VCard>

    <template #footer>
      <VSpace>
        <VButton type="secondary" @click="submitForm">
          {{ t('core.common.buttons.submit') }}
        </VButton>
        <VButton @click="closeModal">
          {{ t('core.common.buttons.cancel_and_shortcut') }}
        </VButton>
      </VSpace>
    </template>
  </VModal>
</template>

<style>
@import "@halo-dev/components/dist/style.css";
</style>
