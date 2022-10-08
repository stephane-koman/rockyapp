import { Col, Form, Input, Modal, Row } from "antd";
import { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { productTypeService } from "../../../services/product-type.service";
import { EActionType, EAgainType } from "../../../utils/enums/global.enum";
import { IProductType } from "../../../utils/interfaces/product-type.interface";
import "./ProductType.modal.scss";

interface IProps {
  isOpen: boolean;
  type?: EActionType;
  productType?: IProductType;
  onClose: (change?: boolean) => void;
}

export const ProductTypeModal = ({
  isOpen,
  type,
  productType,
  onClose,
}: IProps) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const [form] = Form.useForm();
  const [addAgain, setAddAgain] = useState<boolean>(false);

  useEffect(() => {
    if (isOpen && productType) {
      form.setFieldsValue(productType);
    }

    if (isOpen && !productType) {
      form.resetFields();
    }
  }, [productType, isOpen, form]);

  const onCloseHandler = (change?: boolean) => {
    form.resetFields();
    if (!addAgain) {
      onClose(change);
    } 
    setAddAgain(false);
  };

  const onCreateProductType = (data: IProductType) => {
    startTransition(() => {
      productTypeService
        .create(data)
        .then((_) => {
          onCloseHandler(true);
        })
        .catch((err: any) => console.error(err));
    });
  };

  const onUpdateProductType = (data: IProductType) => {
    if (productType) {
      startTransition(() => {
        productTypeService
          .update(productType.id, data)
          .then((_) => {
            onCloseHandler(true);
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const onFinishHandler = (values: any) => {
    if (productType) onUpdateProductType(values);
    else onCreateProductType(values);
  };

  const onCancelHandler = () => {
    setAddAgain(false);
    onClose();
  }

  return (
    <Modal
      className="ProductType"
      open={isOpen}
      destroyOnClose
      title={t(`product_type.${type}_product_type`)}
      onCancel={() => onCancelHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          again={
            type === EActionType.CREATE
              ? {
                  text: t("common.product_type"),
                  type: EAgainType.Un,
                }
              : undefined
          }
          type={type}
          loading={isPending}
          addAgain={addAgain}
          onAddAgain={(check: boolean) => setAddAgain(check)}
          onClose={onCloseHandler}
          onSubmit={form.submit}
        />
      }
    >
      <Form
        form={form}
        name="productType-ref"
        scrollToFirstError
        labelCol={{
          span: 6,
        }}
        wrapperCol={{
          span: 18,
        }}
        initialValues={{
          id: "",
          name: "",
          description: "",
        }}
        onFinish={onFinishHandler}
      >
        <Input name="id" type="hidden" />

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={24}>
            <Form.Item
              name="name"
              label={t("common.name")}
              rules={[
                {
                  required: true,
                  message: t("required.name"),
                },
              ]}
            >
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={24}>
            <Form.Item name="description" label={t("common.description")}>
              <Input.TextArea disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default ProductTypeModal;
