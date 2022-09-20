import { ConfigProvider } from "antd";
import "antd/dist/antd.css";
import enUS from "antd/lib/locale/en_US";
import frFR from "antd/lib/locale/fr_FR";
import { Suspense } from "react";
import { useTranslation } from "react-i18next";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import "./App.scss";
import GuardedRoute from "./components/Guard/GuardedRoute";
import Spinner from "./components/Spinner/Spinner";
import { CLayout } from "./containers/CLayout";
import { AuthProvider } from "./context/auth";
import Login from "./pages/login/Login";
import { Language } from "./utils/enums/language.enum";

function App() {
  const { i18n } = useTranslation();

  return (
    <ConfigProvider
      locale={i18n && i18n.language === Language.EN ? enUS : frFR}
      virtual={false}
    >
      <AuthProvider>
        <BrowserRouter>
          <Suspense fallback={<Spinner size="large" height="100vh" />}>
            <Routes>
              <Route path="/login" element={<Login />} />
              <Route element={<GuardedRoute />}>
                <Route path="*" element={<CLayout />} />
              </Route>
            </Routes>
          </Suspense>
        </BrowserRouter>
      </AuthProvider>
    </ConfigProvider>
  );
}

export default App;
