import {
  CheckOutlined,
  CloseOutlined,
  UnlockOutlined,
} from "@ant-design/icons";
import { Button, Switch, Table } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import PageTitle from "../../components/PageTitle/PageTitle";
import TableActions from "../../components/TableActions/TableActions";
import { getColumnSearchProps } from "../../components/TableColumnComponents/TableColumnComponents";
import TableHeaderActions from "../../components/TableHeaderActions/TableHeaderActions";
import { customerService } from "../../services/customer.service";
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
} from "../../utils/constants/global.constant";
import { ROLE_PERMISSIONS } from "../../utils/constants/permissions.constant";
import {
  EActionType,
  ETableActionType,
  ETableChange,
} from "../../utils/enums/global.enum";
import { getUserPermissions } from "../../utils/helpers/auth.helper";
import {
  showTotalPagination,
  switchStatus,
} from "../../utils/helpers/global.helper";
import {
  getUserManyPermissionsFromList,
  getUserOnePermissionFromList,
} from "../../utils/helpers/permission.helper";
import {
  getActiveListData,
  getColumnFilter,
  getColumnSorter,
  setOneSortsTable,
  setPaginationValues,
} from "../../utils/helpers/table.helper";
import {
  ICustomer,
  ICustomerCriteriaSearch,
} from "../../utils/interfaces/customer.interface";
import { IPagination } from "../../utils/interfaces/global.interface";
import CustomerModal from "./modal/Customer.modal";

enum columnType {
  Name = "name",
  Email = "email",
  Mobile = "mobile",
  Fixe = "fixe",
  Address = "address",
  Active = "active",
}

const initFiliters: ICustomerCriteriaSearch = {
  name: undefined,
  email: undefined,
  mobile: undefined,
  fixe: undefined,
  text_search: undefined,
};

const Customer = () => {
  const userConnectedPermissions: any[] = getUserPermissions();
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [refresh, setRefresh] = useState<boolean>(true);
  const [customerList, setCustomerList] = useState<ICustomer[]>([]);
  const [actionType, setActionType] = useState<EActionType>(EActionType.READ);
  const [customer, setCustomer] = useState<ICustomer | undefined>(undefined);
  const [filters, setFilters] = useState<ICustomerCriteriaSearch>(initFiliters);
  const [reset, setReset] = useState<boolean>(false);

  const [pagination, setPagination] = useState<IPagination>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    total: 0,
  });

  const getColumns = () => {
    const columns: any[] = [
      {
        title: t("common." + columnType.Name),
        dataIndex: columnType.Name,
        key: columnType.Name,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Name, filters),
        sortOrder: getColumnSorter(columnType.Name, pagination.sorts),
        ...getColumnSearchProps(columnType.Name, reset),
      },
      {
        title: t("common." + columnType.Email),
        dataIndex: columnType.Email,
        key: columnType.Email,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Email, filters),
        sortOrder: getColumnSorter(columnType.Email, pagination.sorts),
        ...getColumnSearchProps(columnType.Email, reset),
      },
      {
        title: t("common." + columnType.Mobile),
        dataIndex: columnType.Mobile,
        key: columnType.Mobile,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Mobile, filters),
        sortOrder: getColumnSorter(columnType.Mobile, pagination.sorts),
        ...getColumnSearchProps(columnType.Mobile, reset),
      },
      {
        title: t("common." + columnType.Fixe),
        dataIndex: columnType.Fixe,
        key: columnType.Fixe,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Fixe, filters),
        sortOrder: getColumnSorter(columnType.Fixe, pagination.sorts),
        ...getColumnSearchProps(columnType.Fixe, reset),
      },
      {
        title: t("common.status"),
        key: columnType.Active,
        dataIndex: columnType.Active,
        filters: getActiveListData(t),
        filteredValue: getColumnFilter(columnType.Active, filters),
        render: (_: any, data: ICustomer) => (
          <Switch
            checkedChildren={<CheckOutlined />}
            unCheckedChildren={<CloseOutlined />}
            checked={data.active}
            disabled={
              getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.UPDATE + "_customer"
              ) === null &&
              getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.DELETE + "_customer"
              ) === null
            }
            onChange={(checked: boolean) =>
              onChangeSwitchHandler(checked, data.id)
            }
          />
        ),
      },
    ];

    if (
      ROLE_PERMISSIONS.some((p: string) => userConnectedPermissions.includes(p))
    ) {
      columns.push({
        title: t("common.actions"),
        dataIndex: "",
        key: "x",
        render: (_: any, data: any) => (
          <TableActions
            type={
              getUserManyPermissionsFromList(
                userConnectedPermissions,
                "customer"
              ).length > 2
                ? ETableActionType.DROPDOWN
                : ETableActionType.BUTTON
            }
            data={data}
            permission={{
              read: getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.READ
              ),
              update: getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.UPDATE
              ),
              delete: getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.DELETE
              ),
            }}
            deleteInfo={`${t("common.confirm_delete_info.cet")} ${t(
              "common.customer"
            ).toLowerCase()}?`}
            handleAction={handleModal}
            onConfirmDelete={onConfirmDelete}
          />
        ),
      });
    }
    return columns;
  };

  const onChangeSwitchHandler = (checked: boolean, customerId: number) => {
    const newCustomerList: ICustomer[] = switchStatus(
      checked,
      customerId,
      customerList,
      "customer"
    );
    setCustomerList(newCustomerList);
  };

  const onConfirmDelete = (userId: any) => {
    customerService.delete(userId).then((_: any) => {
      setRefresh(true);
    });
  };

  useEffect(() => {
    if (refresh) {
      searchCustomers();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refresh]);

  const searchCustomers = () => {
    startTransition(() => {
      customerService
        .search(filters, pagination)
        .then((res) => {
          const data = res?.data;
          setCustomerList(data?.results);
          setPaginationValues(data, setPagination);
        })
        .catch((err: any) => {
          console.log(err);
        })
        .finally(() => {
          setRefresh(false);
          if (reset) setReset(false);
        });
    });
  };

  const handleModal = (r: any, type: EActionType) => {
    setActionType(type);
    setCustomer(r);
    setIsModalVisible(true);
  };

  const onChange = (
    currentPagination: any,
    currentFilters: any,
    sorter: any,
    extra: any
  ) => {
    switch (extra?.action) {
      case ETableChange.PAGINATE:
        setPagination({
          ...pagination,
          page: currentPagination?.current,
          size: currentPagination?.pageSize,
        });
        setRefresh(true);
        break;

      case ETableChange.FILTER:
        const data = {
          name: currentFilters.name ? currentFilters.name[0] : undefined,
          email: currentFilters.email ? currentFilters.email[0] : undefined,
          mobile: currentFilters.mobile ? currentFilters.mobile[0] : undefined,
          fixe: currentFilters.fixe ? currentFilters.fixe[0] : undefined,
          active: currentFilters.active
            ? currentFilters.active[0]
              ? 1
              : 0
            : undefined,
        };
        
        if (
          Object.values(data).some((d: any) => d !== undefined && d !== null)
        ) {
          setFilters(data);
          setRefresh(true);
        }
        break;

      case ETableChange.SORT:
        setOneSortsTable(sorter, pagination.sorts, setPagination);
        setRefresh(true);
        break;

      default:
        break;
    }
  };

  const onCloseModal = (change?: boolean) => {
    if (change) {
      setRefresh(true);
    }
    setIsModalVisible(false);
    setCustomer(undefined);
  };

  const onRefresh = () => {
    setPagination({
      page: DEFAULT_PAGE,
      size: DEFAULT_PAGE_SIZE,
      total: 0,
    });
    setFilters(initFiliters);
    setRefresh(true);
    setReset(true);
  };

  const showModal = () => {
    setActionType(EActionType.CREATE);
    setIsModalVisible(true);
  };

  const onSearchInput = (value: string) => {
    setFilters((f: any) => ({
      ...f,
      text_search: value,
    }));
    setRefresh(true);
  };

  return (
    <div>
      <CustomerModal
        type={actionType}
        customer={customer}
        isOpen={isModalVisible}
        onClose={onCloseModal}
      />
      <PageTitle title={t("customer.page_title")} />
      <TableHeaderActions
        search
        refresh
        onSearch={onSearchInput}
        onRefresh={onRefresh}
      >
        {userConnectedPermissions.includes(
          getUserOnePermissionFromList(ROLE_PERMISSIONS, EActionType.CREATE)
        ) && (
          <Button icon={<UnlockOutlined />} type="primary" onClick={showModal}>
            {t("customer.create_customer")}
          </Button>
        )}
      </TableHeaderActions>

      <Table
        rowKey="id"
        dataSource={customerList}
        columns={getColumns()}
        loading={isPending}
        pagination={{
          defaultPageSize: DEFAULT_PAGE_SIZE,
          defaultCurrent: DEFAULT_PAGE,
          current: pagination.page,
          pageSize: pagination.size,
          total: pagination.total,
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total, range) => showTotalPagination(total, range, t),
        }}
        onChange={onChange}
      />
    </div>
  );
};

export default Customer;
