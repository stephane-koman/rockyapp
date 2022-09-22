import { Menu, MenuProps } from "antd";
import memoize from "memoize-one";

import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";
import { MenuItem, recursiveMenu } from "../utils/helpers/menu.helper";
import navigation from "./_nav";

interface IProps {
  currPath: string;
  onClick?: () => void;
}

export const MenuAD = memoize(({currPath, onClick}: IProps) => {
  const { t } = useTranslation();
  const [route, setRoute] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if(route && route !== currPath) {
      navigate(route);
      onClick && onClick();
    }
  }, [route])
  

  const onClickMenu: MenuProps["onClick"] = (e) => {
    setRoute(e.key);
  };

  const items: MenuItem[] =
    (navigation.length > 0 &&
      navigation(t).map((nav: any) =>
        recursiveMenu(nav)
      )) ||
    [];
    
  return (
    <Menu
      selectedKeys={[currPath === "/" ? "/dashboard" : currPath]}
      theme="dark"
      mode="inline"
      items={items}
      onClick={onClickMenu}
    />
  );
});
