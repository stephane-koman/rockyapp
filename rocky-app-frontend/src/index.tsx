import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import { ConfigProvider } from "antd";
import "./i18n";
import frFR from "antd/lib/locale/fr_FR";
import enUS from "antd/lib/locale/en_US";
import { Language } from "./utils/enums/language.enum";
import reportWebVitals from "./reportWebVitals";

const lang: any = localStorage.getItem("i18nextLng");

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

root.render(
  <ConfigProvider
    locale={lang && lang === Language.EN ? enUS : frFR}
    virtual={false}
  >
    <App />
  </ConfigProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
