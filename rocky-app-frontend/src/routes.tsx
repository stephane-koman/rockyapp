import { TFunction } from "i18next";
import React from "react";
import { CUSTOMER_PERMISSIONS, GLOBAL_PERMISSIONS, PERMISSION_PERMISSIONS, PRODUCT_PERMISSIONS, PRODUCT_TYPE_PERMISSIONS, ROLE_PERMISSIONS, USER_PERMISSIONS, VOLUME_PERMISSIONS } from "./utils/constants/permissions.constant";

const Dashboard = React.lazy(() => import('./pages/dashborad/Dashboard'));
const User = React.lazy(() => import("./pages/user/User"));
const Role = React.lazy(() => import("./pages/role/Role"));
const Permission = React.lazy(() => import("./pages/permission/Permission"));
const Customer = React.lazy(() => import("./pages/customer/Customer"));
const Volume = React.lazy(() => import("./pages/volume/Volume"));
const ProductType = React.lazy(() => import("./pages/product-type/ProductType"));
const Product = React.lazy(() => import("./pages/product/Product"));

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
  {
    path: "/customers",
    name: t("menu.customers"),
    breadcrumbName: t("menu.customers"),
    component: Customer,
    permissions: CUSTOMER_PERMISSIONS,
  },
  {
    path: "/settings/*",
    name: t("menu.settings"),
    breadcrumbName: t("menu.settings"),
    component: User,
    permissions: GLOBAL_PERMISSIONS,
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
  {
    path: "/settings/volumes",
    name: t("menu.volumes"),
    breadcrumbName: t("menu.volumes"),
    component: Volume,
    permissions: VOLUME_PERMISSIONS,
  },
  {
    path: "/settings/product_types",
    name: t("menu.product_types"),
    breadcrumbName: t("menu.product_types"),
    component: ProductType,
    permissions: PRODUCT_TYPE_PERMISSIONS,
  },
  {
    path: "/settings/products",
    name: t("menu.products"),
    breadcrumbName: t("menu.products"),
    component: Product,
    permissions: PRODUCT_PERMISSIONS,
  },
];

export default routes;