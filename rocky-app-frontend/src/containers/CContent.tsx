import { Content } from "antd/lib/layout/layout";
import { Suspense } from "react";
import { useTranslation } from "react-i18next";
import { Navigate, Outlet, Route, Routes } from "react-router-dom";
import Spinner from "../components/Spinner/Spinner";
import NotFound from "../pages/not-found/NotFound";
import routes from "../routes";
import { getUserPermissions } from "../utils/helpers/auth.helper";

export const CContent = () => {
  const { t } = useTranslation();
  const permissions: any[] = getUserPermissions();

  const getRoutes = () => {
    const routesFormatted = routes(t)
      .map((route, idx) => {
        return (
          (route?.permissions?.some((r: any) => permissions?.includes(r)) ||
            !route?.permissions) &&
          route.component && (
            <Route key={idx} path={route.path} element={<route.component />} />
          )
        );
      })
      .filter((route: any) => route !== undefined && route !== null);

    return routesFormatted;
  };

  return (
    <Content style={{ margin: "0 16px 16px 16px", overflowY: "auto" }}>
      <div
        className="site-layout-background"
        style={{
          overflowY: "auto",
          padding: "16px",
          height: "100%",
        }}
      >
        <Suspense fallback={<Spinner size="large" height="100vh" />}>
          <Routes>
            {getRoutes()}
            <Route path="/" element={<Navigate replace to="/dashboard" />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </Suspense>
      </div>
    </Content>
  );
};
