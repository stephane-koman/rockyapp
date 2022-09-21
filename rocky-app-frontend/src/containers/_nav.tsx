import {
  AppstoreAddOutlined, DashboardOutlined,
  SettingOutlined,
  TeamOutlined,
  ToolOutlined
} from "@ant-design/icons";
import { TFunction } from "i18next";
import { PERMISSION_PERMISSIONS, ROLE_PERMISSIONS, USER_PERMISSIONS } from "../utils/constants/permissions.constant";

const _nav = (t: TFunction) => [
  {
    name: t("menu.dashboard"),
    to: "/dashboard",
    icon: <DashboardOutlined />,
  },
  /* {
    name: t("menu.customers"),
    to: "/customers",
    icon: <UsergroupAddOutlined />,
    permissions: CUSTOMER_PERMISSIONS,
  }, */
  {
    name: t("menu.settings"),
    to: "/settings",
    icon: <SettingOutlined />,
    permissions: USER_PERMISSIONS.concat(ROLE_PERMISSIONS).concat(
      PERMISSION_PERMISSIONS
    ),
    _children: [
      {
        name: t("menu.users"),
        to: "/settings/users",
        icon: <TeamOutlined />,
        permissions: USER_PERMISSIONS,
      },
      {
        name: t("menu.roles"),
        to: "/settings/roles",
        icon: <ToolOutlined />,
        permissions: ROLE_PERMISSIONS,
      },
      {
        name: t("menu.permissions"),
        to: "/settings/permissions",
        icon: <AppstoreAddOutlined />,
        permissions: PERMISSION_PERMISSIONS,
      },
    ],
  },
];

export default _nav;