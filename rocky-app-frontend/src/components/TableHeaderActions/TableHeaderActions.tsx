import React, { useState, useTransition } from "react";
import { Button, Input, Space } from "antd";
import { SyncOutlined } from "@ant-design/icons";
import "./TableHeaderActions.scss";
import { ITableHeaderActions } from "../../utils/interfaces/global.interface";
import { useTranslation } from "react-i18next";

const TableHeaderActions = ({
  search,
  refresh,
  children,
  onRefresh,
  onSearch,
}: ITableHeaderActions) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [searchInput, setSearchInput] = useState<string>("")

  const onChangeHandler = (e: any) => {
    const value = e?.target?.value;
    setSearchInput(value);
    onSearch && onSearch(value);
  };

  const onRefreshHandler = () => {
    setSearchInput("");
    onRefresh && onRefresh();
  }

  return (
    <Space
      className="TableHeaderActions"
      style={{
        marginBottom: 24,
        width: "100%",
        justifyContent: "space-between",
      }}
    >
      {search && (
        <Input.Search
          value={searchInput}
          loading={isPending}
          allowClear
          onChange={onChangeHandler}
        />
      )}
      <div>
        {refresh && (
          <Button
            style={{
              marginRight: 4,
            }}
            icon={<SyncOutlined />}
            onClick={onRefreshHandler}
          >
            {t("common.refresh")}
          </Button>
        )}
        {children}
      </div>
    </Space>
  );
};

export default TableHeaderActions;
