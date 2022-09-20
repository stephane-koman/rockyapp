import { Layout, message } from "antd";
import "antd/dist/antd.css";
import { TFunction } from "i18next";
import { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/auth";
import { userService } from "../services/user.service";
import { CContent } from "./CContent";
import { CFooter } from "./CFooter";
import { CHeader } from "./CHeader";
import "./CLayout.scss";
import CSidebar from "./CSidebar";
import {
  SmileOutlined,
} from "@ant-design/icons";
import { useTranslation } from "react-i18next";
import { USER_INFOS } from "../utils/constants/global.constant";

const config = ({ name }: any, t: TFunction | any) => {
  return {
    content: (
      <span>
        {t("common.welcome")},{" "}
        <span className="text-capitalize font-weight-bold">{name}</span>!
      </span>
    ),
    icon: <SmileOutlined />,
  };
};

export const CLayout = () => {
  const [isCollapsed, setIsCollapsed] = useState<boolean>(false);
  const authContext = useContext(AuthContext);
  const { t } = useTranslation();

  const onCollapseHandler = (collapsed: boolean) => {
    setIsCollapsed(collapsed);
  };

  const onToggleHandler = () => {
    setIsCollapsed(!isCollapsed);
  };

  useEffect(() => {
    const userInfos: any = localStorage.getItem(USER_INFOS);
    
    if(!userInfos){
      userService.getUserInfos().then((res: any) => {
        message.success(config(res.data, t));
        authContext.setUser(res.data);
      });
    }else{
      authContext.setUser(JSON.parse(userInfos));
    }
    
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <Layout
      className="CLayout"
      style={{
        position: "fixed",
        top: 0,
        right: 0,
        left: 0,
        bottom: 0,
      }}
    >
      <CSidebar collapsed={isCollapsed} onCollapse={onCollapseHandler} />
      <Layout className="site-layout" style={{ flexGrow: 5 }}>
        <CHeader collapsed={isCollapsed} onToggle={onToggleHandler} />
        <CContent />
        <CFooter />
      </Layout>
    </Layout>
  );
};
