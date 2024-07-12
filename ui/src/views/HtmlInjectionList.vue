<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import {
  VCard,
  VPageHeader,
  VButton,
  IconAddCircle,
  VEntity,
  VEntityField,
  VSwitch,
  VDropdownItem,
  Dialog,
} from '@halo-dev/components';
import Fuse from "fuse.js";
import axios from "axios";
import dayjs from "dayjs";
import timezone from "dayjs/plugin/timezone";
import utc from "dayjs/plugin/utc";
import HtmlInjectionAdd from "./HtmlInjectionAdd.vue";
import { useI18n } from "vue-i18n";
import type { HtmlInjection, HtmlInjectionList } from "@/types";

// 初始化 Day.js 插件
dayjs.extend(timezone);
dayjs.extend(utc);

// 使用 i18n
const { t } = useI18n();

// 变量定义
const fuse = ref<Fuse<HtmlInjection> | null>(null);
const htmlInjections = ref<HtmlInjectionList>({
  page: 1,
  size: 20,
  total: 0,
  items: [],
  first: true,
  last: false,
  hasNext: false,
  hasPrevious: false,
  totalPages: 0,
});
const currentHtmlInjection = ref<HtmlInjection | null>(null);
const keyword = ref("");
const isModalVisible = ref(false);
const activeTab = ref("All");

// Axios 实例
const http = axios.create({
  baseURL: "/",
  timeout: 1000,
});


// 函数定义
// 获取代码注入列表
const fetchHtmlInjections = async () => {
  const response = await http.get<HtmlInjectionList>("/apis/theme.halo.run/v1alpha1/htmlinjections");
  htmlInjections.value = response.data;
  fuse.value = new Fuse(response.data.items, {
    keys: ['spec.name', 'spec.description'],
    threshold: 0.3,
  });
  //console.log('fetch data:', response.data);

};

// 打开模态窗口
const openModal = () => {
  currentHtmlInjection.value = null;  // 创建新注入时清空 currentHtmlInjection
  isModalVisible.value = true;
};

// 关闭模态窗口
const closeModal = () => {
  isModalVisible.value = false;
};

// 表单提交
const handleFormSubmit = () => {
  fetchHtmlInjections();
  closeModal();
};

// 跳转到注入详情页面
const detailHtmlInjection = (htmlInjection: HtmlInjection) => {
  console.log('TODO-跳转到详情页面');
};

// 编辑代码注入
const editHtmlInjection = (htmlInjection: HtmlInjection) => {
  currentHtmlInjection.value = htmlInjection;
  isModalVisible.value = true;
};

// 删除代码注入
const deleteHtmlInjection = (htmlInjection: HtmlInjection) => {
  Dialog.warning({
    title: t("删除代码注入"),
    description: t("确认要删除该代码注入吗？"),
    confirmText: t("确认"),
    cancelText: t("取消"),
    onConfirm: () => {
      http
        .delete(`/apis/theme.halo.run/v1alpha1/htmlinjections/${htmlInjection.metadata.name}`)
        .then(() => {
          setTimeout(() => {
            fetchHtmlInjections(); // 延迟重新获取数据，因为发现如果不延迟页面并没有立即反映删除的结果，必须手动刷新页面才能看到变化
          }, 500); // 等待数据库完成更新再获取数据
        })
        .catch((error) => {
          console.error('Error deleting htmlInjection:', error);
        });
    },
  });
};

// 切换代码注入启用状态
const handleToggle = (htmlInjection: HtmlInjection) => {
  http
    .put<HtmlInjection>(
      `/apis/theme.halo.run/v1alpha1/htmlinjections/${htmlInjection.metadata.name}`,
      htmlInjection
    )
    .then(() => {
      fetchHtmlInjections();
    });
};

// 格式化日期时间
const formatDatetime = (date: string | Date | undefined | null, tz?: string): string => {
  return date ? dayjs(date).tz(tz).format("YYYY-MM-DD HH:mm") : "";
};

// 计算属性定义
// 过滤后的代码注入列表
const filteredHtmlInjections = computed(() => {
  let results = htmlInjections.value.items;
  if (keyword.value && fuse.value) {
    results = fuse.value.search(keyword.value).map(result => result.item);
  }
  if (activeTab.value === "Enabled") {
    return results.filter(htmlInjection => htmlInjection.spec.enabled);
  }
  if (activeTab.value === "Disabled") {
    return results.filter(htmlInjection => !htmlInjection.spec.enabled);
  }
  return results;
});

// 计算过滤后的代码注入总数
const filteredTotal = computed(() => {
  return filteredHtmlInjections.value.length;
});

// 在组件挂载时获取代码注入列表
onMounted(fetchHtmlInjections);

</script>

<template>
  <div class="html-injection-list">
    <!-- 页面标题 -->
    <VPageHeader :title="t('代码注入管理')">
      <template #actions>
        <VButton type="secondary" @click="openModal">
          <template #icon>
            <IconAddCircle class="h-full w-full" />
          </template>
          {{ t('新增注入') }}
        </VButton>
      </template>
    </VPageHeader>

    <div class="m-0 md:m-4">
      <VCard :body-class="['!p-0']">
        <!-- 头部包含搜索和标签 -->
        <template>
          <header class="header">
            <div class="block w-full bg-gray-50 px-4 py-3">
              <div class="relative flex flex-col items-start sm:flex-row sm:items-center">
                <div class="flex w-full flex-1 sm:w-auto">
                  <SearchInput v-model="keyword" placeholder="t('请输入关键字')" />
                </div>
                <div>
                  <VButton :class="{ active: activeTab === 'All' }" @click="activeTab = 'All'">{{ t('全部') }}</VButton>
                  <VButton :class="{ active: activeTab === 'Enabled' }" @click="activeTab = 'Enabled'">{{ t('启用') }}</VButton>
                  <VButton :class="{ active: activeTab === 'Disabled' }" @click="activeTab = 'Disabled'">{{ t('禁用') }}</VButton>
                </div>
              </div>
            </div>
          </header>
        </template>

        <!-- 代码注入列表 -->
        <ul class="box-border h-full w-full divide-y divide-gray-100">
          <li v-for="(htmlInjection, index) in filteredHtmlInjections" :key="index">
            <VEntity>
              <template #start>
                <VEntityField
                  :title="htmlInjection.spec.name"
                  :description="htmlInjection.spec.description"
                ></VEntityField>
              </template>
              <template #end>
                <VEntityField>
                  <template #description>
                    <span class="truncate text-xs tabular-nums text-gray-500">
                      {{ formatDatetime(htmlInjection.metadata.creationTimestamp) }}
                    </span>
                  </template>
                </VEntityField>
                <VSwitch
                  v-model="htmlInjection.spec.enabled"
                  @click="() => handleToggle(htmlInjection)"
                />
              </template>
              <template #dropdownItems>
                <VDropdownItem type="danger" @click="deleteHtmlInjection(htmlInjection)">
                  <!-- @ts-ignore -->
                  {{ t("core.common.buttons.delete") }}
                </VDropdownItem>
                <VDropdownItem @click="editHtmlInjection(htmlInjection)">
                  {{ t("修改") }}
                </VDropdownItem>
                <VDropdownItem @click="detailHtmlInjection(htmlInjection)">
                  {{ t("详情") }}
                </VDropdownItem>
              </template>
            </VEntity>
          </li>
        </ul>

        <!-- 页脚显示总数 -->
        <template #footer>
          <div class="flex h-8 items-center">
            <span class="text-sm text-gray-500">
              {{ t("core.components.pagination.total_label", { total: filteredTotal }) }}
            </span>
          </div>
        </template>
      </VCard>
    </div>

    <!-- 添加/编辑代码注入的模态窗口 -->
    <HtmlInjectionAdd
      :visible="isModalVisible"
      :htmlInjection="currentHtmlInjection"
      @close="closeModal"
      @submit="handleFormSubmit"
    />
  </div>
</template>

<style>
@import "@halo-dev/components/dist/style.css";
</style>
