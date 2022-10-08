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
import { volumeService } from "../../services/volume.service";
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
  IVolume,
  IVolumeCriteriaSearch,
} from "../../utils/interfaces/volume.interface";
import { IPagination } from "../../utils/interfaces/global.interface";
import VolumeModal from "./modal/Volume.modal";
import { EMesure } from "../../utils/enums/mesure.enum";

enum columnType {
  Quantity = "quantity",
  Mesure = "mesure",
  Active = "active",
}

const initFiliters: IVolumeCriteriaSearch = {
  quantity: undefined,
  mesure: undefined,
  active: undefined,
  text_search: undefined,
};

const Volume = () => {
  const userConnectedPermissions: any[] = getUserPermissions();
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [refresh, setRefresh] = useState<boolean>(true);
  const [volumeList, setVolumeList] = useState<IVolume[]>([]);
  const [actionType, setActionType] = useState<EActionType>(EActionType.READ);
  const [volume, setVolume] = useState<IVolume | undefined>(undefined);
  const [filters, setFilters] = useState<IVolumeCriteriaSearch>(initFiliters);
  const [reset, setReset] = useState<boolean>(false);

  const [pagination, setPagination] = useState<IPagination>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    total: 0,
  });

  const getColumns = () => {
    const mesureList = Object.values(EMesure).map((m: string) => ({
      value: m,
      text: m,
    }));
    
    const columns: any[] = [
      {
        title: t("common." + columnType.Quantity),
        dataIndex: columnType.Quantity,
        key: columnType.Quantity,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Quantity, filters),
        sortOrder: getColumnSorter(columnType.Quantity, pagination.sorts),
        ...getColumnSearchProps(columnType.Quantity, reset),
      },
      {
        title: t("common." + columnType.Mesure),
        dataIndex: columnType.Mesure,
        key: columnType.Mesure,
        sorter: true,
        filters: mesureList,
        filteredValue: getColumnFilter(columnType.Mesure, filters),
        sortOrder: getColumnSorter(columnType.Mesure, pagination.sorts)
      },
      {
        title: t("common.status"),
        key: columnType.Active,
        dataIndex: columnType.Active,
        filters: getActiveListData(t),
        filteredValue: getColumnFilter(columnType.Active, filters),
        render: (_: any, data: IVolume) => (
          <Switch
            checkedChildren={<CheckOutlined />}
            unCheckedChildren={<CloseOutlined />}
            checked={data.active}
            disabled={
              getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.UPDATE + "_volume"
              ) === null &&
              getUserOnePermissionFromList(
                ROLE_PERMISSIONS,
                EActionType.DELETE + "_volume"
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
              getUserManyPermissionsFromList(userConnectedPermissions, "volume")
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
            deleteInfo={`${t("common.confirm_delete_info.default")} ${t(
              "common.volume"
            ).toLowerCase()}?`}
            handleAction={handleModal}
            onConfirmDelete={onConfirmDelete}
          />
        ),
      });
    }
    return columns;
  };

  const onChangeSwitchHandler = (checked: boolean, volumeId: number) => {
    const newVolumeList: IVolume[] = switchStatus(
      checked,
      volumeId,
      volumeList,
      "volume"
    );
    setVolumeList(newVolumeList);
  };

  const onConfirmDelete = (userId: any) => {
    volumeService.delete(userId).then((_: any) => {
      setRefresh(true);
    });
  };

  useEffect(() => {
    if (refresh) {
      searchVolumes();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refresh]);

  const searchVolumes = () => {
    startTransition(() => {
      volumeService
        .search(filters, pagination)
        .then((res) => {
          const data = res?.data;
          setVolumeList(data?.results);
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
    setVolume(r);
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
        
        setFilters(data);
        setRefresh(true);
        break;

      case ETableChange.SORT:
        setOneSortsTable(sorter, setPagination);
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
    setVolume(undefined);
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
      <VolumeModal
        type={actionType}
        volume={volume}
        isOpen={isModalVisible}
        onClose={onCloseModal}
      />
      <PageTitle title={t("volume.page_title")} />
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
            {t("volume.create_volume")}
          </Button>
        )}
      </TableHeaderActions>

      <Table
        rowKey="id"
        dataSource={volumeList}
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

export default Volume;
