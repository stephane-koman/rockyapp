import { IPagination } from "../interfaces/global.interface";

export const getColumnFilter = (title: any, filters: any) => {
  const column = filters[title];

  const columsActive: any = column !== null && column !== undefined
    ? Array.isArray(column) && column.length > 0
      ? column.concat([title])
      : title === "active" ? [ Boolean(column), title] : [title]
    : [];

  return columsActive;
};

export const getColumnSorter = (title: string, sorts?: string[] | any) => {
  let sort: string | null = null;
  if (sorts) {
    const sortFind: string | undefined = sorts?.find((sIndex: string) =>
      sIndex.includes(title)
    );
    if (sortFind) sort = sortFind.split(",")[1];
  }
  return sort;
};

export const setOneSortsTable = (
  sorter: any,
  setPagination?: any
) => {
  const sortDir: string = sorter.field + "," + sorter.order;
  const newSorts: string[] = [];

  if (sorter.order) newSorts.push(sortDir);

  setPagination((prevP: any) => ({
    ...prevP,
    sorts: newSorts,
  }));
};

export const setManySortsTable = (
  sorter: any,
  sorts?: string[],
  setPagination?: any
) => {
  const sortDir: string = sorter.field + "." + sorter.order;
  const newSorts: string[] =
    sorts?.filter((sort: string) => !sort.includes(sorter.field)) || [];

  if (sorter.order) newSorts.push(sortDir);

  setPagination((prevP: any) => ({
    ...prevP,
    sorts: newSorts,
  }));
};

export const setPaginationValues = (data: any, setPagination: any) => {
  setPagination((prevPagination: IPagination) => ({
    ...prevPagination,
    page: data.page + 1,
    total: data.total,
  }));
};

export const getActiveListData = (t: any) => [
  {
    value: true,
    text: t("common.enable"),
  },
  {
    value: false,
    text: t("common.disable"),
  },
];
