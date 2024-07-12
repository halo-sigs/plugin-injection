import { definePlugin } from "@halo-dev/console-shared";
import HtmlInjectionList from "./views/HtmlInjectionList.vue";
import { IconPlug } from "@halo-dev/components";
import { markRaw } from "vue";

export default definePlugin({
  components: {},
  routes: [
    {
      parentName: "Root",
      route: {
        path: "/HtmlInjection",
        name: "HtmlInjection",
        component: HtmlInjectionList,
        meta: {
          title: "代码注入管理",
          searchable: true,
          menu: {
            name: "代码注入管理",
            group: "工具",
            icon: markRaw(IconPlug),
            priority: 0,
          },
        },
      },
    },
  ],
  extensionPoints: {},
});
