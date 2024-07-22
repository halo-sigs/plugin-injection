import { definePlugin } from "@halo-dev/console-shared";
import HtmlInjectionList from "./views/HtmlInjectionList.vue";
import { IconPlug } from "@halo-dev/components";
import { markRaw } from "vue";

export default definePlugin({
  components: {},
  routes: [
    {
      parentName: "ToolsRoot",
      route: {
        path: "/html-injection",
        name: "HtmlInjection",
        component: HtmlInjectionList,
        meta: {
          title: "代码注入管理",
          searchable: true,
          menu: {
            name: "代码注入管理",
            icon: markRaw(IconPlug),
            priority: 0,
          },
        },
      },
    },
  ],
  extensionPoints: {},
});
