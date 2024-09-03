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
  const htmlInjection = props.htmlInjection as HtmlInjection;
  const requestData: HtmlInjection = {...htmlInjection, spec: formData};
  axiosInstance.put(`/apis/theme.halo.run/v1alpha1/htmlinjections/${htmlInjection.metadata?.name}`, requestData)
    .then(() => {
      emit('success');
      closeModal();
    })
    .catch(error => {
      console.error('Error updating html injection:', error);
    });
}
</script>

<template>
  <VModal
    :title="'编辑代码注入'"
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
