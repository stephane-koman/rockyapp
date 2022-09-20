import { MenuOutlined } from "@ant-design/icons";
import { Button, Drawer, Image } from "antd";
import { useState } from "react";
import { useLocation } from "react-router-dom";
import LogoRD from "../assets/images/logo-rd.svg";
import { MenuAD } from "./CMenu";

const CNavBar = () => {
  const [open, setOpen] = useState<boolean>(false);
  const currPath = useLocation().pathname;

  const onHideMenuHandler = () => {
    setOpen(false);
  };

  return (
    <div className="navbar">
      <Button
        className="menu"
        type="dashed"
        icon={<MenuOutlined />}
        onClick={() => setOpen(true)}
      />

      <Drawer
        title={<Image height={40} src={LogoRD} preview={false} />}
        placement="left"
        closable={false}
        onClose={onHideMenuHandler}
        open={open}
      >
        <MenuAD currPath={currPath} onClick={onHideMenuHandler} />
      </Drawer>
    </div>
  );
};
export default CNavBar;
