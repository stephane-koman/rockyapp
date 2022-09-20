import React from "react";
import { Button, Dropdown, Menu, Popconfirm, Space } from "antd";
import {
  EyeOutlined,
  EditOutlined,
  DeleteOutlined,
  DownOutlined,
  QuestionCircleOutlined,
} from "@ant-design/icons";
import { ITableActions } from "../../../utils/interfaces/global.interface";
import { EActionType } from "../../../utils/enums/global.enum";
import { useTranslation } from "react-i18next";
import { getUserPermissions } from "../../../utils/helpers/auth.helper";
import { ItemType } from "antd/lib/menu/hooks/useItems";

const TableActionsDropdown = ({
  data,
  items,
  deleteInfo,
  permission,
  handleAction,
  onCancelDelete,
  onConfirmDelete,
  handleOtherAction,
}: ITableActions) => {
  const { t } = useTranslation();
  const userPermissions: any[] = getUserPermissions();

  const handleMenuClick = (e?: any) => {
    switch (e?.key) {
      case EActionType.READ:
        handleAction && handleAction(data, EActionType.READ);
        break;

      case EActionType.UPDATE:
        handleAction && handleAction(data, EActionType.UPDATE);
        break;

      case EActionType.DELETE:
        break;

      default:
        handleOtherAction && handleOtherAction(data, e?.key);
        break;
    }
  };

  const getMenuItems = (): ItemType[] => {
    let itemsTypes: ItemType[] = [];

    if (permission && userPermissions.includes(permission.read)){
      itemsTypes.push({
        key: EActionType.READ,
        icon: <EyeOutlined />,
        label: t("common.show"),
      });
    } 

    if (permission && userPermissions.includes(permission.update)) {
      itemsTypes.push({
        key: EActionType.UPDATE,
        icon: <EditOutlined />,
        label: t("common.edit"),
      });
    }

    if (permission && userPermissions.includes(permission.delete)) {
      itemsTypes.push({
        key: EActionType.DELETE,
        icon: <DeleteOutlined />,
        label: (
          <Popconfirm
            placement="topRight"
            title={deleteInfo}
            icon={<QuestionCircleOutlined style={{ color: "red" }} />}
            onConfirm={() => onConfirmDelete && onConfirmDelete(data?.id)}
            onCancel={onCancelDelete}
            okText={t("common.yes")}
            cancelText={t("common.no")}
          >
            {t("common.delete")}
          </Popconfirm>
        ),
      });
    }

    if(items) itemsTypes = itemsTypes.concat(items);

    return itemsTypes;
  }

  const menu = (
    <Menu onClick={handleMenuClick} items={getMenuItems()} />
  );

  return (
    <Space wrap>
      <Dropdown overlay={menu}>
        <Button>
          {t("common.actions")} <DownOutlined />
        </Button>
      </Dropdown>
    </Space>
  );
};

export default TableActionsDropdown;
