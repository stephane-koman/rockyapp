import React from "react";
import { Button, Tooltip } from "antd";
import { useTranslation } from "react-i18next";
import { EActionType } from "../../../utils/enums/global.enum";
import { EyeOutlined, EditOutlined } from "@ant-design/icons";
import { getUserPermissions } from "../../../utils/helpers/auth.helper";
import { ITableActions } from "../../../utils/interfaces/global.interface";
import DeleteModal from "../../DeleteModal/Delete.modal";

const TableActionsButton = ({
  data,
  children,
  deleteInfo,
  permission,
  handleAction,
  onCancelDelete, 
  onConfirmDelete,
}: ITableActions) => {
  const { t } = useTranslation();
  const userPermissions: any[] = getUserPermissions();
  return (
    <>
      {userPermissions.includes(permission?.read) && (
        <Tooltip title={t("common.show")} zIndex={0}>
          <Button
            icon={<EyeOutlined />}
            type="default"
            onClick={() => handleAction && handleAction(data, EActionType.READ)}
          />
        </Tooltip>
      )}
      {userPermissions.includes(permission?.update) && (
        <Tooltip title={t("common.edit")} zIndex={0}>
          <Button
            icon={<EditOutlined />}
            type="primary"
            onClick={() => handleAction && handleAction(data, EActionType.UPDATE)}
          />
        </Tooltip>
      )}
      {userPermissions.includes(permission?.delete) && (
        <DeleteModal
          id={data?.id}
          info={deleteInfo}
          onCancel={() => onCancelDelete && onCancelDelete()}
          onConfirm={() => onConfirmDelete && onConfirmDelete(data?.id)}
        />
      )}
      {children}
    </>
  );
};

export default TableActionsButton;
