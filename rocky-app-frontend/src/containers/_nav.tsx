import {
  AppstoreAddOutlined,
  DashboardOutlined,
  SettingOutlined,
  TeamOutlined,
  ToolOutlined,
  UsergroupAddOutlined,
  BgColorsOutlined
} from "@ant-design/icons";
import { TFunction } from "i18next";
import { CUSTOMER_PERMISSIONS, PERMISSION_PERMISSIONS, ROLE_PERMISSIONS, USER_PERMISSIONS, VOLUME_PERMISSIONS } from "../utils/constants/permissions.constant";

const _nav = (t: TFunction) => [
  {
    name: t("menu.dashboard"),
    to: "/dashboard",
    icon: <DashboardOutlined />,
  },
  {
    name: t("menu.customers"),
    to: "/customers",
    icon: <UsergroupAddOutlined />,
    permissions: CUSTOMER_PERMISSIONS,
  },
  {
    name: t("menu.settings"),
    to: "/settings",
    icon: <SettingOutlined />,
    permissions: USER_PERMISSIONS.concat(ROLE_PERMISSIONS)
      .concat(PERMISSION_PERMISSIONS)
      .concat(VOLUME_PERMISSIONS),
    _children: [
      {
        name: t("menu.volumes"),
        to: "/settings/volumes",
        icon: <BgColorsOutlined />,
        permissions: VOLUME_PERMISSIONS,
      },
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