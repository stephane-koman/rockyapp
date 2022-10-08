import {
  CheckOutlined,
  CloseOutlined, UserAddOutlined
} from "@ant-design/icons";
import { Button, Switch, Table } from "antd";
import React, { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import PageTitle from "../../components/PageTitle/PageTitle";
import TableActions from "../../components/TableActions/TableActions";
import { getColumnSearchProps } from "../../components/TableColumnComponents/TableColumnComponents";
import TableHeaderActions from "../../components/TableHeaderActions/TableHeaderActions";
import { productTypeService } from "../../services/product-type.service";
import { productService } from "../../services/product.service";
import { volumeService } from "../../services/volume.service";
import {
  DEFAULT_PAGE,
  DEFAULT_PAGE_SIZE,
  DEVISE
} from "../../utils/constants/global.constant";
import { PRODUCT_PERMISSIONS } from "../../utils/constants/permissions.constant";
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
import { IProductType } from "../../utils/interfaces/product-type.interface";
import {
  IProductCriteriaSearch,
  ISimpleProduct
} from "../../utils/interfaces/product.interface";
import { IVolume } from "../../utils/interfaces/volume.interface";
import ProductModal from "./modal/Product.modal";

enum columnType {
  Code = "code",
  Name = "name",
  Price = "price",
  ProductTypeTitle = "product_type",
  ProductType = "productType",
  ProductTypeList = "productTypeList",
  Volume = "volume",
  VolumeList = "volumeList",
  Active = "active",
}

const initFilters: IProductCriteriaSearch = {
  text_search: undefined,
  code: undefined,
  name: undefined,
  price: undefined,
  productTypeList: undefined,
  volumeList: undefined,
};

const Product: React.FC = () => {
  const productConnectedPermissions: any[] = getUserPermissions();
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [isModalVisible, setIsModalVisible] = useState<boolean>(false);
  useState<boolean>(false);
  const [refresh, setRefresh] = useState<boolean>(true);
  const [reset, setReset] = useState<boolean>(false);
  const [products, setProducts] = useState<ISimpleProduct[]>([]);
  const [volumeList, setVolumeList] = useState<IVolume[]>([]);
  const [productTypeList, setProductTypeList] = useState<IProductType[]>([]);
  const [product, setProduct] = useState<ISimpleProduct | undefined>(undefined);
  const [actionType, setActionType] = useState<EActionType>(EActionType.READ);
  const [filters, setFilters] = useState<IProductCriteriaSearch>(initFilters);

  const [pagination, setPagination] = useState<IPagination>({
    page: DEFAULT_PAGE,
    size: DEFAULT_PAGE_SIZE,
    total: 0,
  });

  const getColumns = () => {
    const productTypesT: any[] = productTypeList.map((p) => ({
      value: p.name,
      text: p.name,
    }));

    const volumesT: any[] = volumeList.map((v) => ({
      value: v.quantity + v.mesure,
      text: v.quantity + v.mesure,
    }));

    const columns: any[] = [
      {
        title: t("product." + columnType.Code),
        dataIndex: columnType.Code,
        key: columnType.Code,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Code, filters),
        sortOrder: getColumnSorter(columnType.Code, pagination.sorts),
        ...getColumnSearchProps(columnType.Code, reset),
        /* render: (_: any, data: ISimpleProduct) => (
          <div>
            <Barcode format="EAN13" value={data.code} width={1} height={40} />
          </div>
        ), */
      },
      {
        title: t("common." + columnType.ProductTypeTitle),
        key: columnType.ProductType,
        dataIndex: columnType.ProductType,
        filters: productTypesT,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.ProductTypeList, filters),
        sortOrder: getColumnSorter(columnType.ProductType, pagination.sorts),
        render: (_: any, data: ISimpleProduct) => data?.productType?.name,
      },
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
        title: t("common." + columnType.Price),
        dataIndex: columnType.Price,
        key: columnType.Price,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.Price, filters),
        sortOrder: getColumnSorter(columnType.Price, pagination.sorts),
        ...getColumnSearchProps(columnType.Price, reset),
        render: (_: any, data: ISimpleProduct) => (
          <div>
            {data?.price} {DEVISE}
          </div>
        ),
      },
      {
        title: t("common." + columnType.Volume),
        key: columnType.Volume,
        dataIndex: columnType.Volume,
        filters: volumesT,
        sorter: true,
        filterSearch: true,
        filteredValue: getColumnFilter(columnType.VolumeList, filters),
        sortOrder: getColumnSorter(columnType.Volume, pagination.sorts),
        render: (_: any, data: ISimpleProduct) => (
          <div>
            {data?.volume?.quantity} {data?.volume?.mesure}
          </div>
        ),
      },
      {
        title: t("common.status"),
        key: columnType.Active,
        dataIndex: columnType.Active,
        sorter: true,
        filters: getActiveListData(t),
        filteredValue: getColumnFilter(columnType.Active, filters),
        sortOrder: getColumnSorter(columnType.Active, pagination.sorts),
        render: (_: any, data: ISimpleProduct) => (
          <Switch
            checkedChildren={<CheckOutlined />}
            unCheckedChildren={<CloseOutlined />}
            checked={data.active}
            disabled={
              getUserOnePermissionFromList(
                PRODUCT_PERMISSIONS,
                EActionType.UPDATE + "_product"
              ) === null &&
              getUserOnePermissionFromList(
                PRODUCT_PERMISSIONS,
                EActionType.DELETE + "_product"
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
      PRODUCT_PERMISSIONS.some((p: string) => productConnectedPermissions.includes(p))
    ) {
      columns.push({
        title: t("common.actions"),
        dataIndex: "",
        key: "x",
        render: (_: any, data: ISimpleProduct) => (
          <TableActions
            type={
              getUserManyPermissionsFromList(productConnectedPermissions, "product")
                .length > 2
                ? ETableActionType.DROPDOWN
                : ETableActionType.BUTTON
            }
            data={data}
            permission={{
              read: getUserOnePermissionFromList(
                PRODUCT_PERMISSIONS,
                EActionType.READ + "_product"
              ),
              update: getUserOnePermissionFromList(
                PRODUCT_PERMISSIONS,
                EActionType.UPDATE + "_product"
              ),
              delete: getUserOnePermissionFromList(
                PRODUCT_PERMISSIONS,
                EActionType.DELETE + "_product"
              ),
            }}
            deleteInfo={`${t("common.confirm_delete_info.default")} ${t(
              "common.product"
            ).toLowerCase()}?`}
            handleAction={handleModal}
            onConfirmDelete={onConfirmDelete}
          />
        ),
      });
    }

    return columns;
  };

  const onChangeSwitchHandler = (checked: boolean, productId: number) => {
    const productList: ISimpleProduct[] = switchStatus(
      checked,
      productId,
      products,
      "product"
    );
    setProducts(productList);
  };

  const onConfirmDelete = (productId: any) => {
    productService.delete(productId).then((_: any) => {
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

    productTypeService.search(criteria, {
      ...page,
      sorts: ["name,asc"]
    }).then((res) => {
      setProductTypeList(res.data?.results);
    });

    volumeService.search(criteria, {
      ...page,
      sorts: ["quantity,asc", "mesure,asc"]
    }).then((res) => {
      setVolumeList(res?.data?.results);
    });

    setReset(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (refresh) {
      startTransition(() => {
        productService
          .search(filters, pagination)
          .then((res) => {
            const data = res?.data;
            setProducts(data?.results);
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
    setProduct(u);
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
          code: currentFilters.code ? currentFilters.code[0] : undefined,
          name: currentFilters.name ? currentFilters.name[0] : undefined,
          price: currentFilters.price ? currentFilters.price[0] : undefined,
          productTypeList: currentFilters.productType
            ? currentFilters.productType?.length > 0
              ? currentFilters.productType
              : undefined
            : undefined,
          volumeList: currentFilters.volume
            ? currentFilters.volume?.length > 0
              ? currentFilters.volume
              : undefined
            : undefined,
          active: currentFilters.active
            ? currentFilters.active?.length > 0
              ? currentFilters.active[0]
                ? 1
                : 0
              : undefined
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
    setProduct(undefined);
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
      <ProductModal
        type={actionType}
        product={product}
        productTypeList={productTypeList}
        volumeList={volumeList}
        isOpen={isModalVisible}
        onClose={onCloseModal}
      />
      <PageTitle title={t("product.page_title")} />
      <TableHeaderActions
        search
        refresh
        onSearch={onSearchInput}
        onRefresh={onRefresh}
      >
        {productConnectedPermissions.includes(
          getUserOnePermissionFromList(PRODUCT_PERMISSIONS, EActionType.CREATE)
        ) && (
          <Button icon={<UserAddOutlined />} type="primary" onClick={showModal}>
            {t("product.create_product")}
          </Button>
        )}
      </TableHeaderActions>
      <Table
        rowKey="id"
        dataSource={products}
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

export default React.memo(Product);
