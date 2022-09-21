import { TFunction } from "i18next";
import React from "react";
import { PERMISSION_PERMISSIONS, ROLE_PERMISSIONS, USER_PERMISSIONS } from "./utils/constants/permissions.constant";

const Dashboard = React.lazy(() => import('./pages/dashborad/Dashboard'));
const User = React.lazy(() => import("./pages/user/User"));
const Role = React.lazy(() => import("./pages/role/Role"));
const Permission = React.lazy(() => import("./pages/permission/Permission"));
//const Customer = React.lazy(() => import("../old_codes/customer/Customer"));

const routes = (t: TFunction) => [
  {
    path: "/",
    name: t("common.home"),
    breadcrumbName: t("common.home"),
    component: Dashboard,
  },
  {
    path: "/dashboard",
    name: t("menu.dashboard"),
    breadcrumbName: t("menu.dashboard"),
    component: Dashboard,
  },
  /* {
    path: "/customers",
    name: t("menu.customers"),
    breadcrumbName: t("menu.customers"),
    component: Customer,
    permissions: CUSTOMER_PERMISSIONS,
  }, */
  {
    path: "/settings/*",
    name: t("menu.settings"),
    breadcrumbName: t("menu.settings"),
    component: User,
    permissions: USER_PERMISSIONS.concat(ROLE_PERMISSIONS).concat(
      PERMISSION_PERMISSIONS
    ),
  },
  {
    path: "/settings/users",
    name: t("menu.users"),
    breadcrumbName: t("menu.users"),
    component: User,
    permissions: USER_PERMISSIONS,
  },
  {
    path: "/settings/roles",
    name: t("menu.roles"),
    breadcrumbName: t("menu.roles"),
    component: Role,
    permissions: ROLE_PERMISSIONS,
  },
  {
    path: "/settings/permissions",
    name: t("menu.permissions"),
    breadcrumbName: t("menu.permissions"),
    component: Permission,
    permissions: PERMISSION_PERMISSIONS,
  },
];

export default routes;