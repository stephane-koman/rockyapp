import { red } from "@ant-design/colors";
import {
  FrownOutlined, LockOutlined, LoginOutlined, UserOutlined
} from "@ant-design/icons";
import {
  Button,
  Card, Form, Image, Input, Layout, message,
  Select
} from "antd";
import { TFunction } from "i18next";
import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { Navigate, useLocation, useNavigate } from "react-router-dom";
import LogoRD from "../../assets/images/logo-rd-outline.svg";
import { AuthContext } from "../../context/auth";
import i18n from "../../i18n";
import { globalService } from "../../services/global.service";
import { SITE_TITLE } from "../../utils/constants/global.constant";
import { Language } from "../../utils/enums/language.enum";
import { userIsLoggedIn } from "../../utils/helpers/auth.helper";
import "./Login.scss";

const configSession = (t: TFunction | any) => {
  return {
    content: <span>{t("common.session_expired")}</span>,
    icon: <FrownOutlined />,
  };
};

const Login = () => {
  const { t } = useTranslation();
  const [lang, setLang] = useState<Language>(i18n.language as Language);
  const context = useContext(AuthContext);
  const [form] = Form.useForm();
  const [error, setError] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(false);
  const isAuthenticated = userIsLoggedIn();
  const location = useLocation();
  const navigate = useNavigate();

  const onFinish = (data: any) => {
    setLoading(true);
    globalService
      .login(data)
      .then((res: any) => {
        const userData: any = res?.data;
        context.login(userData);
        navigate("/");
      })
      .catch((err: any) => {
        setLoading(false);
        setError(err);
      });
  };

  const onChangeLanguage = (selectedValue: any) => {
    const language = selectedValue;

    switch (language) {
      case Language.EN:
        setLang(Language.EN);
        i18n.changeLanguage(Language.EN);
        localStorage.setItem("i18nextLng", Language.EN);
        break;
      case Language.FR:
      default:
        setLang(Language.FR);
        i18n.changeLanguage(Language.FR);
        localStorage.setItem("i18nextLng", Language.FR);
        break;
    }
  };

  useEffect(() => {
    const expired = new URLSearchParams(location.search).get("session");
    if (expired) {
      message.error(configSession(t));
      navigate("/login");
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  
  if (isAuthenticated) {
    return <Navigate to="/" />;
  }

  return (
    <div className="Login">
      <Select
        defaultValue={lang}
        style={{
          width: 70,
          marginRight: "8px",
          marginTop: "8px",
          position: "absolute",
          right: 0,
          background: "#FFF",
        }}
        bordered={false}
        onChange={onChangeLanguage}
      >
        <Select.Option value={Language.FR}>FR</Select.Option>
        <Select.Option value={Language.EN}>EN</Select.Option>
      </Select>
      <Layout style={{ minHeight: "100vh" }}>
        <div
          style={{
            width: "40%",
            height: "100%",
            margin: "auto",
          }}
        >
          <div
            style={{
              width: "100%",
              marginBottom: "10%",
              textAlign: "center",
              color: "#FFF",
              fontSize: "2rem",
            }}
          >
            <div style={{ marginBottom: "1.5rem" }}>
              <Image height={80} width={80} src={LogoRD} preview={false} />
            </div>
            <div>{SITE_TITLE}</div>
          </div>
          <Card
            title={t("login.title")}
            style={{
              marginRight: "auto",
              marginLeft: "auto",
            }}
            headStyle={{ textAlign: "center", fontSize: "30px" }}
          >
            {error && (
              <div style={{ color: red.primary, padding: "0 30px", textAlign: "center" }}>
                {t("login.auth_error")}
              </div>
            )}
            <Form
              form={form}
              style={{
                width: "100%",
                marginRight: "auto",
                marginLeft: "auto",
                padding: "30px",
                backgroundColor: "#FFFFFF",
              }}
              name="normal_login"
              size="large"
              initialValues={{
                email: "",
                password: "",
              }}
              onFinish={onFinish}
            >
              <Form.Item
                name="email"
                rules={[
                  {
                    required: true,
                    type: "email",
                    message: t("login.email_required"),
                  },
                ]}
              >
                <Input
                  className="ant-input-login"
                  prefix={<UserOutlined className="site-form-item-icon" />}
                  type="email"
                  placeholder={t("common.email")}
                />
              </Form.Item>

              <Form.Item
                name="password"
                rules={[
                  {
                    required: true,
                    message: t("login.password_required"),
                  },
                ]}
              >
                <Input.Password
                  className="ant-input-login"
                  prefix={<LockOutlined className="site-form-item-icon" />}
                  placeholder={t("common.password")}
                />
              </Form.Item>

              <Form.Item shouldUpdate>
                {() => (
                  <Button
                    className="ant-btn-login"
                    type="primary"
                    htmlType="submit"
                    block
                    loading={loading}
                    icon={<LoginOutlined />}
                    disabled={
                      !form.isFieldsTouched(true) ||
                      !!form
                        .getFieldsError()
                        .filter((data) => data.errors.length).length
                    }
                  >
                    {t("login.sign_in")}
                  </Button>
                )}
              </Form.Item>
            </Form>
          </Card>
        </div>
      </Layout>
    </div>
  );
};

export default Login;
