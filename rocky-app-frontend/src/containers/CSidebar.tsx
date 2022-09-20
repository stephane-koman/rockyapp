import { Image, Layout } from "antd";
import { useLocation } from "react-router-dom";
import LogoRD from "../assets/images/logo-rd.svg";
// Menu config
import { MenuAD } from "./CMenu";

const { Sider } = Layout;

interface IProps {
  collapsed: boolean;
  onCollapse: (collapsed: boolean) => void;
}

const CSidebar = ({ collapsed, onCollapse }: IProps) => {
  const currPath = useLocation().pathname;

  return (
    <Sider
      style={{ flexGrow: 1 }}
      collapsible
      collapsed={collapsed}
      onCollapse={onCollapse}
    >
      <div className="logo">
        <Image height={40} src={LogoRD} preview={false} />
      </div>
      <MenuAD currPath={currPath} />
    </Sider>
  );
};

export default CSidebar;
