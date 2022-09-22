import { MenuProps } from "antd";
import { Link } from "react-router-dom";
import { getUserPermissions } from "./auth.helper";

export type MenuItem = Required<MenuProps>["items"][number];

const getItem = (
  label: React.ReactNode,
  key?: React.Key | null,
  icon?: React.ReactNode,
  children?: MenuItem[],
  type?: "group"
): MenuItem => {
  return {
    key,
    icon,
    children,
    label,
    type,
  } as MenuItem;
};

export const recursiveMenu = (nav: any): MenuItem => {
  if (
    (!nav?._children &&
      nav?.permissions?.some((p: any) => getUserPermissions()?.includes(p))) ||
    (!nav?._children && !nav?.permissions)
  ) {
    return getItem(<Link to={nav?.to}>{nav?.name}</Link>, nav?.to, nav?.icon);
  } else {
    return (
      (nav?.permissions?.some((p: any) => getUserPermissions()?.includes(p)) ||
        !nav?.permissions) &&
      getItem(
        nav?.name,
        nav?.to,
        nav?.icon,
        nav?._children?.map((navChild: any) =>
          recursiveMenu(navChild)
        )
      )
    );
  }
};
