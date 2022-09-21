import { TFunction } from "i18next";
import { globalService } from "../../services/global.service";

export const showTotalPagination = (
  total: number,
  range: any[],
  t: TFunction
) => `${range[0]}-${range[1]} ${t("common.to")} ${total} ${t("common.items")}`;

export const addSortsForSearch = (
  sortsParam: string[] | undefined
): string[] => {
  const sorts: string[] = [];

  if (sortsParam) {
    sortsParam?.forEach((sort: string) => {
      sort = sort.replace("ascend", "asc");
      sort = sort.replace("descend", "desc");
      sorts.push(sort);
    });
  }

  return sorts;
};

export const globalFilterOption = (
  inputValue: string,
  option: any,
  field: string
): boolean =>
  option[field]?.toUpperCase().indexOf(inputValue?.toUpperCase()) > -1;

export const switchStatus = (
  checked: boolean,
  id: number,
  items: any[],
  endPoint: string
): any[] => {
  const itemList: any[] = [...items];

  const index = itemList.findIndex((item: any) => item.id === id);
  itemList[index].active = checked;

  globalService
    .updateStatus(
      id,
      {
        active: checked,
      },
      endPoint
    )
    .then((_) => {});

  return itemList;
};
