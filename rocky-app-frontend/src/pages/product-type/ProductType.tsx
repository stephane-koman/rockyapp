import {
  CheckOutlined,
  CloseOutlined,
  UnlockOutlined
} from "@ant-design/icons";
import { Button, Switch, Table } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import PageTitle from "../../components/PageTitle/PageTitle";
import TableActions from "../../components/TableActions/TableActions";
import { getColumnSearchProps } from "../../components/TableColumnComponents/TableColumnComponents";
import TableHeaderActions from "../../components/TableHeaderActions/TableHeaderActions";
import { productTypeService } from "../../services/product-type.service";
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE
} from "../../utils/constants/global.constant";
import { PRODUCT_TYPE_PERMISSIONS } from "../../utils/constants/permissions.constant";
import {
  EActionType,
  ETableActionType,
  ETableChange
} from "../../utils/enums/global.enum";
import { getUserPermissions } from "../../utils/helpers/auth.helper";
import {
  showTotalPagination,
  switchStatus
} from "../../utils/helpers/global.helper";
import {
  getUserManyPermissionsFromList,
  getUserOnePermissionFromList
} from "../../utils/helpers/permission.helper";
import {
  getActiveListData,
  getColumnFilter,
  getColumnSorter,
  setOneSortsTable,
  setPaginationValues
} from "../../utils/helpers/table.helper";
import { IPagination } from "../../utils/interfaces/global.interface";
import {
  IProductType,
  IProductTypeCriteriaSearch
} from "../../utils/interfaces/product-type.interface";
import ProductTypeModal from "./modal/ProductType.modal";

enum columnType {
  Name = "name",
  Active = "active",
}

const initFiliters: IProductTypeCriteriaSearch = {
  name: undefined,
  active: undefined,
  text_search: undefined,
};

const ProductType = () => {
  const userConnectedPermissions: any[] = getUserPermissions();
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  const [refresh, setRefresh] = useState<boolean>(true);
  const [productTypeList, setProductTypeList] = useState<IProductType[]>([]);
  const [actionType, setActionType] = useState<EActionType>(EActionType.READ);
  const [productType, setProductType] = useState<IProductType | undefined>(undefined);
  const [filters, setFilters] = useState<IProductTypeCriteriaSearch>(initFiliters);
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
        title: t("common.status"),
        key: columnType.Active,
        dataIndex: columnType.Active,
        sorter: true,
        filters: getActiveListData(t),
        filteredValue: getColumnFilter(columnType.Active, filters),
        sortOrder: getColumnSorter(columnType.Active, pagination.sorts),
        render: (_: any, data: IProductType) => (
          <Switch
            checkedChildren={<CheckOutlined />}
            unCheckedChildren={<CloseOutlined />}
            checked={data.active}
            disabled={
              getUserOnePermissionFromList(
                PRODUCT_TYPE_PERMISSIONS,
                EActionType.UPDATE + "_product_type"
              ) === null &&
              getUserOnePermissionFromList(
                PRODUCT_TYPE_PERMISSIONS,
                EActionType.DELETE + "_product_type"
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
      PRODUCT_TYPE_PERMISSIONS.some((p: string) => userConnectedPermissions.includes(p))
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
                "product_type"
              ).length > 2
                ? ETableActionType.DROPDOWN
                : ETableActionType.BUTTON
            }
            data={data}
            permission={{
              read: getUserOnePermissionFromList(
                PRODUCT_TYPE_PERMISSIONS,
                EActionType.READ
              ),
              update: getUserOnePermissionFromList(
                PRODUCT_TYPE_PERMISSIONS,
                EActionType.UPDATE
              ),
              delete: getUserOnePermissionFromList(
                PRODUCT_TYPE_PERMISSIONS,
                EActionType.DELETE
              ),
            }}
            deleteInfo={`${t("common.confirm_delete_info.default")} ${t(
              "common.product_type"
            ).toLowerCase()}?`}
            handleAction={handleModal}
            onConfirmDelete={onConfirmDelete}
          />
        ),
      });
    }
    return columns;
  };

  const onChangeSwitchHandler = (checked: boolean, productTypeId: number) => {
    const newProductTypeList: IProductType[] = switchStatus(
      checked,
      productTypeId,
      productTypeList,
      "product-type"
    );
    setProductTypeList(newProductTypeList);
  };

  const onConfirmDelete = (userId: any) => {
    productTypeService.delete(userId).then((_: any) => {
      setRefresh(true);
    });
  };

  useEffect(() => {
    if (refresh) {
      searchProductTypes();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [refresh]);

  const searchProductTypes = () => {
    startTransition(() => {
      productTypeService
        .search(filters, pagination)
        .then((res) => {
          const data = res?.data;
          setProductTypeList(data?.results);
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
    setProductType(r);
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
    setProductType(undefined);
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
      <ProductTypeModal
        type={actionType}
        productType={productType}
        isOpen={isModalVisible}
        onClose={onCloseModal}
      />
      <PageTitle title={t("product_type.page_title")} />
      <TableHeaderActions
        search
        refresh
        onSearch={onSearchInput}
        onRefresh={onRefresh}
      >
        {userConnectedPermissions.includes(
          getUserOnePermissionFromList(PRODUCT_TYPE_PERMISSIONS, EActionType.CREATE)
        ) && (
          <Button icon={<UnlockOutlined />} type="primary" onClick={showModal}>
            {t("product_type.create_product_type")}
          </Button>
        )}
      </TableHeaderActions>

      <Table
        rowKey="id"
        dataSource={productTypeList}
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

export default ProductType;
