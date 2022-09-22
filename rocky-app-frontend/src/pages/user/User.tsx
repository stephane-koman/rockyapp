import {
  CheckOutlined,
  CloseOutlined,
  LockOutlined,
  UserAddOutlined,
} from "@ant-design/icons";
import { Button, Switch, Table } from "antd";
import { ItemType } from "antd/lib/menu/hooks/useItems";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import PageTitle from "../../components/PageTitle/PageTitle";
import TableActions from "../../components/TableActions/TableActions";
import { getColumnSearchProps } from "../../components/TableColumnComponents/TableColumnComponents";
import TableHeaderActions from "../../components/TableHeaderActions/TableHeaderActions";
import { globalService } from "../../services/global.service";
import { permissionService } from "../../services/permission.service";
import { roleService } from "../../services/role.service";
import { userService } from "../../services/user.service";
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
} from "../../utils/constants/global.constant";
import { USER_PERMISSIONS } from "../../utils/constants/permissions.constant";
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
import { IPermission } from "../../utils/interfaces/permission.interface";
import { ISimpleRole } from "../../utils/interfaces/role.interface";
import {
  ISimpleUser,
  IUserCriteriaSearch,
} from "../../utils/interfaces/user.interface";
import UserModal from "./modal/User.modal";
import UserPasswordModal from "./modal/UserPassword.modal";

enum columnType {
  Name = "name",
  Username = "username",
  Email = "email",
  Roles = "roles",
  RoleList = "roleList",
  Active = "active",
}

const initFilters: IUserCriteriaSearch = {
  name: undefined,
  username: undefined,
  email: undefined,
  text_search: undefined,
  roleList: undefined,
};

const User = () => {
  const userConnectedPermissions: any[] = getUserPermissions();
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  useState<boolean>(false);
  const [refresh, setRefresh] = useState<boolean>(true);
  const [reset, setReset] = useState<boolean>(false);
  const [showModalPassword, setShowModalPassword] = useState<boolean>(false);
  const [users, setUsers] = useState<ISimpleUser[]>([]);
  const [roleList, setRoleList] = useState<ISimpleRole[]>([]);
  const [permissionList, setPermissionList] = useState<IPermission[]>([]);
  const [user, setUser] = useState<ISimpleUser | undefined>(undefined);
  const [actionType, setActionType] = useState<EActionType>(EActionType.READ);
  const [filters, setFilters] = useState<IUserCriteriaSearch>(initFilters);

  const [pagination, setPagination] = useState<IPagination>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    total: 0,
  });

  const getColumns = () => {
    const rolesT: any[] = roleList.map((r) => ({
      value: r.name,
      text: r.name,
    }));

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
        title: t("common." + columnType.Username),
        dataIndex: columnType.Username,
        key: columnType.Username,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Username, filters),
        sortOrder: getColumnSorter(columnType.Username, pagination.sorts),
        ...getColumnSearchProps(columnType.Username, reset),
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
        title: t("common." + columnType.Roles),
        key: columnType.RoleList,
        dataIndex: columnType.RoleList,
        filters: rolesT,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.RoleList, filters),
        render: (_: any, data: ISimpleUser) => (
          <div>{data?.roleList?.join(", ")}</div>
        ),
      },
      {
        title: t("common.status"),
        key: columnType.Active,
        dataIndex: columnType.Active,
        filters: getActiveListData(t),
        filteredValue: getColumnFilter(columnType.Active, filters),
        render: (_: any, data: ISimpleUser) => (
          <Switch
            checkedChildren={<CheckOutlined />}
            unCheckedChildren={<CloseOutlined />}
            checked={data.active}
            disabled={
              getUserOnePermissionFromList(
                USER_PERMISSIONS,
                EActionType.UPDATE + "_user"
              ) === null &&
              getUserOnePermissionFromList(
                USER_PERMISSIONS,
                EActionType.DELETE + "_user"
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
      USER_PERMISSIONS.some((p: string) => userConnectedPermissions.includes(p))
    ) {
      columns.push({
        title: t("common.actions"),
        dataIndex: "",
        key: "x",
        render: (_: any, data: ISimpleUser) => (
          <TableActions
            type={
              getUserManyPermissionsFromList(userConnectedPermissions, "user")
                .length > 2
                ? ETableActionType.DROPDOWN
                : ETableActionType.BUTTON
            }
            data={data}
            permission={{
              read: getUserOnePermissionFromList(
                USER_PERMISSIONS,
                EActionType.READ + "_user"
              ),
              update: getUserOnePermissionFromList(
                USER_PERMISSIONS,
                EActionType.UPDATE + "_user"
              ),
              delete: getUserOnePermissionFromList(
                USER_PERMISSIONS,
                EActionType.DELETE + "_user"
              ),
            }}
            deleteInfo={`${t("common.confirm_delete_info.cet")} ${t(
              "common.user"
            ).toLowerCase()}?`}
            handleAction={handleModal}
            handleOtherAction={handlePasswordModal}
            onConfirmDelete={onConfirmDelete}
            items={getCustomItems()}
          />
        ),
      });
    }

    return columns;
  };

  const onChangeSwitchHandler = (checked: boolean, userId: number) => {
    const userList: ISimpleUser[] = switchStatus(
      checked,
      userId,
      users,
      "user"
    );
    setUsers(userList);
  };

  const getCustomItems = (): ItemType[] => {
    if (
      userConnectedPermissions.includes(
        getUserOnePermissionFromList(USER_PERMISSIONS, EActionType.UPDATE)
      )
    ) {
      return [
        {
          key: "password",
          icon: <LockOutlined />,
          label: t("common.password"),
        },
      ];
    }
    return [];
  };

  const onConfirmDelete = (userId: any) => {
    userService.delete(userId).then((_: any) => {
      setRefresh(true);
    });
  };

  useEffect(() => {
    const criteria: any = {
      active: 1,
    };

    const page: IPagination = {
      size: 1,
    };

    roleService.search(criteria, page).then((res) => {
      setRoleList(res.data?.results);
    });

    permissionService.search(criteria, page).then((res) => {
      setPermissionList(res?.data?.results);
    });

    setReset(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (refresh) {
      startTransition(() => {
        userService
          .search(filters, pagination)
          .then((res) => {
            const data = res?.data;
            setUsers(data?.results);
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
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refresh]);

  const handleModal = (u: any, type: EActionType) => {
    setActionType(type);
    setUser(u);
    setIsModalVisible(true);
  };

  const handlePasswordModal = (u: any) => {
    setUser(u);
    setShowModalPassword(true);
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
          name: currentFilters.name?.le ? currentFilters.name[0] : undefined,
          username: currentFilters.username
            ? currentFilters.username[0]
            : undefined,
          email: currentFilters.email ? currentFilters.email[0] : undefined,
          roleList: currentFilters.roleList
            ? currentFilters.roleList?.length > 0
              ? currentFilters.roleList
              : undefined
            : undefined,
          active: currentFilters.active
            ? currentFilters.active?.length
              ? currentFilters.active[0]
                ? 1
                : 0
              : undefined
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
    setUser(undefined);
  };

  const onCloseModalPassword = () => {
    setShowModalPassword(false);
    setUser(undefined);
  };

  const onRefresh = () => {
    setPagination({
      size: DEFAULT_PAGE_SIZE,
      page: DEFAULT_PAGE,
      total: 0,
    });
    setFilters(initFilters);
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
      <UserPasswordModal
        user={user}
        isOpen={showModalPassword}
        onClose={onCloseModalPassword}
      />
      <UserModal
        type={actionType}
        user={user}
        roleList={roleList}
        permissionList={permissionList}
        isOpen={isModalVisible}
        onClose={onCloseModal}
      />
      <PageTitle title={t("user.page_title")} />
      <TableHeaderActions
        search
        refresh
        onSearch={onSearchInput}
        onRefresh={onRefresh}
      >
        {userConnectedPermissions.includes(
          getUserOnePermissionFromList(USER_PERMISSIONS, EActionType.CREATE)
        ) && (
          <Button icon={<UserAddOutlined />} type="primary" onClick={showModal}>
            {t("user.create_user")}
          </Button>
        )}
      </TableHeaderActions>
      <Table
        rowKey="id"
        dataSource={users}
        columns={getColumns()}
        loading={isPending}
        //scroll={{ scrollToFirstRowOnChange: true, y: "450px" }}
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

export default User;
