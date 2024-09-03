<script setup lang="ts">
import HtmlInjectionForm from './HtmlInjectionForm.vue';
import {axiosInstance} from "@halo-dev/api-client";
import {VButton, VModal, VSpace} from '@halo-dev/components';
import type {HtmlInjection, HtmlInjectionSpec} from "@/types";

const props = defineProps<{
  htmlInjection: HtmlInjection | null;
}>();

const emit = defineEmits(['close', 'success']);

function closeModal() {
  emit('close');
}

function handleFormSubmit(formData: HtmlInjectionSpec) {
  const requestData: HtmlInjection = {
    apiVersion: 'theme.halo.run/v1alpha1',
    kind: 'HtmlInjection',
    metadata: {
      name: '',
      generateName: 'htmlinjection-',
    },
    spec: formData,
  };
  axiosInstance.post('/apis/theme.halo.run/v1alpha1/htmlinjections', requestData)
      .then(() => {
        emit('success');
        closeModal();
      })
      .catch(error => {
        console.error('Error creating html injection:', error);
      });
}
</script>

<template>
  <VModal
      :title="'新增代码注入'"
      :width="700"
      @close="closeModal"
  >
    <HtmlInjectionForm
        :htmlInjection="props.htmlInjection"
        @submit="handleFormSubmit"/>
    <template #footer>
      <VSpace>
        <!-- @vue-ignore -->
        <VButton type="secondary" @click="$formkit.submit('html-injection-form')">保存</VButton>
        <VButton @click="closeModal">取消</VButton>
      </VSpace>
    </template>
  </VModal>
</template>
<style>
@import "@halo-dev/components/dist/style.css";
</style>
