import React, { useEffect, useRef, useState } from "react";
import { ColumnFilterItem, FilterDropdownProps } from "antd/lib/table/interface";
import { useTranslation } from "react-i18next";
import { Button, Checkbox, Divider, Input, List, Menu, Radio } from "antd";
import Item from "antd/lib/list/Item";

interface IProps {
  dataIndex: any;
  filterMultiple?: boolean;
  reset?: boolean;
}

interface IColumnSelectProps extends FilterDropdownProps, IProps {}

interface IPropsCheckComponent {
  filterMultiple?: boolean;
  checked: boolean;
  onChange?: (e: any) => void;
}

export const ColumnSearchProps = ({
  setSelectedKeys,
  selectedKeys,
  confirm,
  clearFilters,
  visible,
  dataIndex,
  reset,
}: IColumnSelectProps) => {
  const [search, setSearch] = useState<string>("");
  const { t } = useTranslation();
  const inputRef = useRef<any>(null);

  useEffect(() => {
    if (visible) {
      inputRef?.current?.focus();
    }
  }, [visible]);

  useEffect(() => {
    if (reset) {
      setSearch("");
    }
  }, [reset]);

  const onChangeHandler = (e: any) => {
    setSearch(e.target.value);
    setSelectedKeys(e.target.value ? [e.target.value] : []);
  };

  const onConfirmHandler = (e: any) => {
    e.stopPropagation();
    confirm();
  }

  const onResetHandler = () => {
    setSearch("");
    handleReset(confirm, clearFilters);
  }
  
  return (
    <div className="custom-filter-dropdown">
      <Input
        ref={inputRef}
        placeholder={`${t("common.search")} ${t(`common.${dataIndex}`)}`}
        value={search}
        onChange={onChangeHandler}
        onPressEnter={onConfirmHandler}
        style={{ width: 200, marginBottom: 8, display: "block" }}
        autoFocus
      />
      <Button
        type="link"
        disabled={selectedKeys.length === 0}
        onClick={onResetHandler}
        size="small"
        style={{ width: 96, marginRight: 8 }}
      >
        {t("common.reset")}
      </Button>
      <Button
        type="primary"
        onClick={() => confirm()}
        size="small"
        style={{ width: 96 }}
      >
        {t("common.search")}
      </Button>
    </div>
  );
};

export const getColumnSearchProps = (dataIndex: any, reset?: boolean) => ({
  filterDropdown: (props: FilterDropdownProps) => (
    <ColumnSearchProps dataIndex={dataIndex} reset={reset} {...props} />
  ),
});

const onInputSelectChange = (
  e: React.ChangeEvent<HTMLInputElement>,
  filters: ColumnFilterItem[] | undefined,
  setFiltersTmp: (selectedKeys: ColumnFilterItem[]) => void,
  setSearch: any
) => {
  const value: any = e?.target?.value;
  setSearch(value);
  const data: any[] = filters && filters.filter((f: any) =>
    f?.value?.toLowerCase()?.includes(value?.toLowerCase())
  ) || [];
  setFiltersTmp(data);
};

const CheckComponent = ({
  filterMultiple,
  checked,
  onChange,
}: IPropsCheckComponent) =>
  filterMultiple ? (
    <Checkbox checked={checked} onChange={onChange} />
  ) : (
    <Radio checked={checked} onChange={onChange} />
  );

const onChangeCheck = (
  e: any,
  key: any,
  selectedKeys: React.Key[],
  setSelectedKeys: (selectedKeys: React.Key[]) => void
) => {
  const selectedKeysTmp: React.Key[] = [...selectedKeys];
  if (!e?.target?.checked) {
    if (selectedKeys.includes(key)) {
      const index: number = selectedKeys.findIndex((s: any) => s === key);
      selectedKeysTmp.splice(index, 1);
    }
  } else {
    selectedKeysTmp.push(key);
  }
  setSelectedKeys(selectedKeysTmp);
};

const onChangeCheckAll = (
  e: any,
  filters: ColumnFilterItem[] | undefined,
  setSelectedKeys: (selectedKeys: React.Key[]) => void
) => {
  if (!e?.target?.checked) {
    setSelectedKeys([]);
  } else {
    setSelectedKeys(
      filters && filters.map((f: ColumnFilterItem) => f.value.toString()) || []
    );
  }
};

export const ColumnSelectProps = ({
  filters,
  setSelectedKeys,
  selectedKeys,
  confirm,
  clearFilters,
  visible,
  dataIndex,
  filterMultiple,
  reset,
}: IColumnSelectProps) => {
  const menuKeys: any[] = selectedKeys;
  const { t } = useTranslation();
  const [filtersTmp, setFiltersTmp] = useState<ColumnFilterItem[] | undefined>(filters);
  const [search, setSearch] = useState<any>(null);
  const inputRef = useRef<any>(null);

  useEffect(() => {
    if (visible) {
      inputRef?.current?.focus();
    }
  }, [visible]);

  useEffect(() => {
    if (reset) {
      setSearch(null);
      setFiltersTmp(filters);
    }
  }, [reset]);

  return (
    <div className="custom-filter-dropdown">
      <Input
        ref={inputRef}
        placeholder={`${t("common.search")} ${t(`common.${dataIndex}`)}`}
        value={search}
        onChange={(e) =>
          onInputSelectChange(e, filters, setFiltersTmp, setSearch)
        }
        onPressEnter={() => confirm()}
        style={{ width: 200, marginBottom: 8, display: "block" }}
      />

      <List>
        <Divider />
        {filterMultiple && (
          <List.Item key="all" style={{ marginBottom: 0, marginTop: 0 }}>
            <CheckComponent
              checked={
                selectedKeys?.length > 0 &&
                selectedKeys?.length === filters?.length
              }
              filterMultiple={filterMultiple}
              onChange={(e: any) =>
                onChangeCheckAll(e, filters, setSelectedKeys)
              }
            />
            <span>
              {t("common.select").toUpperCase() +
                " " +
                t("common.tout").toUpperCase()}
            </span>
          </List.Item>
        )}
        {filtersTmp?.map((filter: any, index: number) => {
          const key = String(filter.value);
          return (
            <List.Item
              key={index}
              style={{ marginBottom: 0, marginTop: 0 }}
            >
              <CheckComponent
                checked={selectedKeys?.includes(key)}
                filterMultiple={filterMultiple}
                onChange={(e) =>
                  onChangeCheck(e, key, selectedKeys, setSelectedKeys)
                }
              />
              <span>{filter.text}</span>
            </List.Item>
          );
        })}
      </List>

      <div className={`custom-dropdown-btns`}>
        <Button
          type="link"
          disabled={selectedKeys?.length === 0 && (search === "" || !search)}
          onClick={() =>
            handleReset(
              confirm,
              clearFilters,
              filters,
              setFiltersTmp,
              setSearch
            )
          }
          size="small"
          style={{ width: 96, marginRight: 8 }}
        >
          {t("common.reset")}
        </Button>
        <Button
          type="primary"
          onClick={() => confirm()}
          size="small"
          style={{ width: 96 }}
        >
          {t("common.search")}
        </Button>
      </div>
    </div>
  );
};

const handleReset = (
  confirm: any,
  clearFilters: any,
  filters?: any,
  setFiltersTmp?: any,
  setSearch?: any
) => {
  if (setFiltersTmp) {
    setFiltersTmp(filters);
  }
  if (setSearch) {
    setSearch(null);
  }
  clearFilters();
  confirm();
};

export const getColumnSelectProps = (
  dataIndex: any,
  filterMultiple: boolean,
  reset?: boolean
) => ({
  filterDropdown: (props: FilterDropdownProps) => (
    <ColumnSelectProps
      dataIndex={dataIndex}
      filterMultiple={filterMultiple}
      reset={reset}
      {...props}
    />
  ),
});
