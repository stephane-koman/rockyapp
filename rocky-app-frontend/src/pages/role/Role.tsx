import {
  UnlockOutlined,
  CheckOutlined,
  CloseOutlined,
} from "@ant-design/icons";
import { Button, Switch, Table } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import PageTitle from "../../components/PageTitle/PageTitle";
import TableActions from "../../components/TableActions/TableActions";
import { getColumnSearchProps } from "../../components/TableColumnComponents/TableColumnComponents";
import TableHeaderActions from "../../components/TableHeaderActions/TableHeaderActions";
import { permissionService } from "../../services/permission.service";
import { roleService } from "../../services/role.service";
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
import { IPagination } from "../../utils/interfaces/global.interface";
import {
  IPermission,
  IPermissionCriteriaSearch,
} from "../../utils/interfaces/permission.interface";
import {
  IRoleCriteriaSearch,
  ISimpleRole,
} from "../../utils/interfaces/role.interface";
import RoleModal from "./modal/Role.modal";

enum columnType {
  Name = "name",
  Description = "description",
  Active = "active",
}

const initFiliters: IRoleCriteriaSearch = {
  name: undefined,
  description: undefined,
  text_search: undefined,
};

const Role = () => {
  const userConnectedPermissions: any[] = getUserPermissions();
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [refresh, setRefresh] = useState<boolean>(true);
  const [roleList, setRoleList] = useState<ISimpleRole[]>([]);
  const [permissionList, setPermissionList] = useState<IPermission[]>([]);
  const [actionType, setActionType] = useState<EActionType>(EActionType.READ);
  const [role, setRole] = useState<ISimpleRole | undefined>(undefined);
  const [filters, setFilters] = useState<IRoleCriteriaSearch>(initFiliters);
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
        title: t("common." + columnType.Description),
        dataIndex: columnType.Description,
        key: columnType.Description,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Description, filters),
        sortOrder: getColumnSorter(columnType.Description, pagination.sorts),
        ...getColumnSearchProps(columnType.Description, reset),
      },
      {
        title: t("common.status"),
        key: columnType.Active,
        dataIndex: columnType.Active,
        filters: getActiveListData(t),
        filteredValue: getColumnFilter(columnType.Active, filters),
        render: (_: any, data: ISimpleRole) => (
          <Switch
            checkedChildren={<CheckOutlined />}
            unCheckedChildren={<CloseOutlined />}
            checked={data.active}
            disabled={
              getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.UPDATE + "_role"
              ) === null &&
              getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.DELETE + "_role"
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
              getUserManyPermissionsFromList(userConnectedPermissions, "role")
                .length > 2
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
              "common.role"
            ).toLowerCase()}?`}
            handleAction={handleModal}
            onConfirmDelete={onConfirmDelete}
          />
        ),
      });
    }
    return columns;
  };

  const onChangeSwitchHandler = (checked: boolean, roleId: number) => {
    const newRoleList: ISimpleRole[] = switchStatus(
      checked,
      roleId,
      roleList,
      "role"
    );
    setRoleList(newRoleList);
  };

  const onConfirmDelete = (userId: any) => {
    roleService.delete(userId).then((_: any) => {
      setRefresh(true);
    });
  };

  useEffect(() => {
    const criteria: IPermissionCriteriaSearch = {
      active: 1,
    };
    const page: IPagination = {
      size: 1,
    };

    permissionService.search(criteria, page).then((res) => {
      setPermissionList(res?.data?.results);
    });

    setReset(false);

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (refresh) {
      searchRoles();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refresh]);

  const searchRoles = () => {
    startTransition(() => {
      roleService
        .search(filters, pagination)
        .then((res) => {
          const data = res?.data;
          setRoleList(data?.results);
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
    setRole(r);
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
          name: currentFilters.name ? currentFilters.name[0] : null,
          description: currentFilters.description
            ? currentFilters.description[0]
            : null,

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
    setRole(undefined);
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
      <RoleModal
        type={actionType}
        role={role}
        permissionList={permissionList}
        isOpen={isModalVisible}
        onClose={onCloseModal}
      />
      <PageTitle title={t("role.page_title")} />
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
            {t("role.create_role")}
          </Button>
        )}
      </TableHeaderActions>

      <Table
        rowKey="id"
        dataSource={roleList}
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

export default Role;
