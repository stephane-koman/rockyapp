import { TFunction } from "i18next";

export const showTotalPagination = (total: number, range: any[], t: TFunction) =>
  `${range[0]}-${range[1]} ${t("common.to")} ${total} ${t("common.items")}`;

export const addSortsForSearch = (sortsParam: string[] | undefined): string[] => {
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

export const globalFilterOption = (inputValue: string, option: any, field: string): boolean =>
  option[field]?.toUpperCase().indexOf(inputValue?.toUpperCase()) > -1;