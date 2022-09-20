import { UnlockOutlined } from "@ant-design/icons";
import { Button, Table } from "antd";
import { useEffect, useRef, useState, useTransition } from "react";
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
import { EActionType, ETableActionType } from "../../utils/enums/global.enum";
import { getUserPermissions } from "../../utils/helpers/auth.helper";
import { showTotalPagination } from "../../utils/helpers/global.helper";
import {
  getUserManyPermissionsFromList,
  getUserOnePermissionFromList,
} from "../../utils/helpers/permission.helper";
import {
  getColumnFilter,
  getColumnSorter,
  setPaginationValues,
  setOneSortsTable,
} from "../../utils/helpers/table.helper";
import { IPagination } from "../../utils/interfaces/global.interface";
import { IPermission } from "../../utils/interfaces/permission.interface";
import {
  IRole,
  IRoleCriteriaSearch,
  ISimpleRole,
} from "../../utils/interfaces/role.interface";
import RoleModal from "./modal/Role.modal";

enum columnType {
  Name = "name",
  Description = "description",
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
            onCancelDelete={onCancelDelete}
          />
        ),
      });
    }
    return columns;
  };

  const onCancelDelete = (e: any) => {
    console.log(e);
  };

  const onConfirmDelete = (userId: any) => {
    roleService.delete(userId).then((_: any) => {
      setRefresh(true);
    });
  };

  useEffect(() => {
    permissionService.search().then((res) => {
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
      case "paginate":
        setPagination({
          ...pagination,
          page: currentPagination?.current,
          size: currentPagination?.pageSize,
        });

        break;

      case "filter":
        setFilters({
          name: currentFilters.name ? currentFilters.name[0] : null,
          description: currentFilters.description
            ? currentFilters.description[0]
            : null,
        });

        break;

      case "sort":
        setOneSortsTable(sorter, pagination.sorts, setPagination);

        break;

      default:
        break;
    }
    setRefresh(true);
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

export default Role;
