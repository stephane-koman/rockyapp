import React, { useEffect, useState, useTransition } from "react";
import { Button, Pagination, Space, Table } from "antd";
import { SyncOutlined } from "@ant-design/icons";
import { useTranslation } from "react-i18next";
import { IPagination } from "../../utils/interfaces/global.interface";
import { permissionService } from "../../services/permission.service";
import { DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_TABLE_KEY } from "../../utils/constants/global.constant";
import { IPermission, IPermissionCriteriaSearch } from "../../utils/interfaces/permission.interface";
import { showTotalPagination } from "../../utils/helpers/global.helper";
import { setPaginationValues } from "../../utils/helpers/table.helper";

const Permission = () => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [refresh, setRefresh] = useState<boolean>(true);
  const [permissionList, setPermissionList] = useState<IPermission[]>([]);
  const [criteriaSearch, setCriteriaSearch] =
    useState<IPermissionCriteriaSearch>({
      text_search: "",
      name: "",
      description: "",
    });

  const [pagination, setPagination] = useState<IPagination>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    total: 0,
  });

  const columns = [
    {
      title: t("common.name"),
      dataIndex: "name",
      key: "name",
    },
    {
      title: t("common.description"),
      dataIndex: "description",
      key: "description",
    },
  ];

  useEffect(() => {
    if (refresh) {      
      searchPermissions();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refresh]);

  const searchPermissions = () => {
    startTransition(() => {
      permissionService
        .search(criteriaSearch, pagination)
        .then((res) => {
          const data = res?.data;
          setPermissionList(data?.results);
          setPaginationValues(data, setPagination);
        })
        .catch((err: any) => {
          console.log(err);
        })
        .finally(() => {
          setRefresh(false);
        });
    });
  }

  const onTableChange = (
    currentPagination: any,
    filters: any,
    sorter: any,
    extra: any
  ) => {
    switch (extra?.action) {
      case "paginate":
        setPagination({
          ...pagination,
          page: currentPagination?.current,
          size: currentPagination?.pageSize,
        });

        setRefresh(true);

        break;

      case "filters":
        console.log(filters);

        break;

      case "sorters":
        console.log(sorter);

        break;

      default:
        break;
    }
  };

  const onRefresh = () => {
    setPagination({
      page: DEFAULT_PAGE,
      size: DEFAULT_PAGE_SIZE,
      total: 0,
    });
    setRefresh(true);
  };

  return (
    <div>
      <Space
        style={{
          marginBottom: 16,
          width: "100%",
          justifyContent: "space-between",
        }}
      >
        <h2 className="">{t("permission.page_title")}</h2>
        <Button icon={<SyncOutlined />} onClick={onRefresh}>
          {t("common.refresh")}
        </Button>
      </Space>
      <Table
        rowKey={DEFAULT_TABLE_KEY}
        dataSource={permissionList}
        columns={columns}
        loading={isPending}
        //scroll={{ scrollToFirstRowOnChange: true, y: "500px" }}
        pagination={{
          current: pagination.page,
          pageSize: pagination.size,
          total: pagination.total,
          showSizeChanger: true,
          showTotal: (total, range) => showTotalPagination(total, range, t),
        }}
        onChange={onTableChange}
      />
    </div>
  );
};

export default Permission;
