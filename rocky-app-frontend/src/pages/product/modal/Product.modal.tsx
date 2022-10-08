import { red } from "@ant-design/colors";
import { PlusOutlined, WarningOutlined } from "@ant-design/icons";
import {
  Carousel,
  Col,
  Divider,
  Form,
  Input,
  InputNumber,
  List,
  Modal,
  Row,
  Select,
  Upload,
  UploadFile,
  UploadProps,
} from "antd";
import { RcFile } from "antd/lib/upload";
import React, { useEffect, useState, useTransition } from "react";
import { useTranslation } from "react-i18next";
import ModalFooterActions from "../../../components/ModalFooterActions/ModalFooterActions";
import { productService } from "../../../services/product.service";
import { DEVISE } from "../../../utils/constants/global.constant";
import { EActionType, EAgainType } from "../../../utils/enums/global.enum";
import { IDocument } from "../../../utils/interfaces/document.interface";
import { IProductType } from "../../../utils/interfaces/product-type.interface";
import {
  IProductCrea,
  ISimpleProduct,
} from "../../../utils/interfaces/product.interface";
import { IVolume } from "../../../utils/interfaces/volume.interface";

const getBase64 = async (file: RcFile): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });

interface IProps {
  isOpen: boolean;
  type?: EActionType;
  product?: ISimpleProduct;
  volumeList?: IVolume[];
  productTypeList?: IProductType[];
  onClose: (change?: boolean) => void;
}

const contentStyle: React.CSSProperties = {
  height: "100%",
  color: "#fff",
  textAlign: "center",
  //background: "#364d79",
};

const ProductModal = ({
  isOpen,
  type,
  product,
  volumeList,
  productTypeList,
  onClose,
}: IProps) => {
  const { t } = useTranslation();
  const [isPending, startTransition] = useTransition();
  const startTransfertTransition = useTransition()[1];
  const [form] = Form.useForm();
  const [addAgain, setAddAgain] = useState<boolean>(false);
  const [previewOpen, setPreviewOpen] = useState<boolean>(false);
  const [previewFileUID, setPreviewFileUID] = useState<string>("");
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [errors, setErrors] = useState<any[]>([]);

  useEffect(() => {
    if (isOpen && product) {
      setFileList([]);
      getProductById();
    }

    if (isOpen && !product) {
      form.resetFields();
      setFileList([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [product, isOpen, form]);

  const getProductById = () => {
    if (product) {
      startTransfertTransition(() => {
        productService
          .findById(product.id)
          .then((res) => {
            const data = res?.data;
            if (data?.documentList) {
              const filesPromise: Promise<UploadFile>[] = base64ToFiles(
                data?.documentList
              );
              Promise.all(filesPromise).then((values) => {
                setFileList(values);
              });
            }

            form.setFieldsValue({
              ...product,
              productTypeId: data?.productType?.id,
              volumeId: data?.volume?.id,
            });
          })
          .catch((err: any) => console.error(err));
      });
    }
  };

  const onCloseHandler = (change?: boolean) => {
    form.resetFields();
    setErrors([]);
    if (!addAgain) {
      onClose(change);
    }
    setAddAgain(false);
  };

  const base64ToFiles = (data: IDocument[]): Promise<UploadFile>[] =>
    data.map(async (d, index) => {
      const res = await fetch(d.content);
      const buff = await res.arrayBuffer();
      const file = new File([buff], d.fileName, { type: d.mimeType });
      return {
        uid: index.toString(),
        name: d.fileName,
        type: d.mimeType,
        thumbUrl: d.content,
        originFileObj: file as RcFile,
      };
    });

  const getBase64FromList = (): Promise<IDocument>[] =>
    fileList?.map(async (file: UploadFile) => {
      return {
        content: await getBase64(file.originFileObj as RcFile),
        fileName: file.name,
        mimeType: file.type,
      } as IDocument;
    });

  const onCreateProduct = (data: IProductCrea) => {
    startTransition(() => {
      if (fileList?.length > 0) {
        const fileListPromise: Promise<IDocument>[] = getBase64FromList();

        Promise.all(fileListPromise).then((values: IDocument[]) => {
          data.documentList = values;
          createProduct(data);
        });
      } else {
        createProduct(data);
      }
    });
  };

  const createProduct = (data: IProductCrea) => {
    productService
      .create(data)
      .then((_) => {
        onCloseHandler(true);
      })
      .catch((err: any) => console.error(err));
  };

  const updateProduct = (data: IProductCrea) => {
    if (product) {
      productService
        .update(product?.id, data)
        .then((_) => {
          onCloseHandler(true);
        })
        .catch((err: any) => console.error(err));
    }
  };

  const onUpdateProduct = (data: IProductCrea) => {
    if (product) {
      startTransition(() => {
        if (fileList?.length > 0) {
          const fileListPromise: Promise<IDocument>[] = getBase64FromList();

          Promise.all(fileListPromise).then((values: IDocument[]) => {
            data.documentList = values;
            updateProduct(data);
          });
        } else {
          updateProduct(data);
        }
      });
    }
  };

  const onFinishHandler = (values: IProductCrea) => {
    if (product) onUpdateProduct(values);
    else onCreateProduct(values);
  };

  const onCancelHandler = () => {
    setAddAgain(false);
    onClose();
  };

  const onChangeImageHandler: UploadProps["onChange"] = ({
    fileList: newFileList,
  }) => {
    setFileList(newFileList);
  };

  const onPreviewImageHandler = async (file: UploadFile) => {
    setPreviewOpen(true);
    setPreviewFileUID(file.uid);
  };

  const getPreviewImage = (file: UploadFile): string => {
    if (!file.url && !file.preview) {
      getBase64(file.originFileObj as RcFile).then((preview) => {
        file.preview = preview;
      });
    }
    return file.url || (file.preview as string);
  };

  const onCancelPreviewHandler = () => setPreviewOpen(false);

  const uploadButton = (
    <div>
      <PlusOutlined />
      <div style={{ marginTop: 8 }}>{t("common.upload")}</div>
    </div>
  );

  return (
    <Modal
      open={isOpen}
      destroyOnClose
      title={t(`product.${type}_product`)}
      width={1000}
      onCancel={() => onCancelHandler()}
      onOk={form.submit}
      footer={
        <ModalFooterActions
          again={
            type === EActionType.CREATE
              ? {
                  text: t("common.product"),
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
        name="product-ref"
        scrollToFirstError
        labelCol={{
          span: 6,
        }}
        wrapperCol={{
          span: 18,
        }}
        initialValues={{
          id: "",
          code: "",
          name: "",
          description: "",
          price: null,
          image: null,
          productTypeId: null,
          volumeId: null,
        }}
        onFinish={onFinishHandler}
      >
        {Object.keys(errors).length > 0 && (
          <List
            dataSource={Object.values(errors)}
            renderItem={(item) => (
              <List.Item>
                <List.Item.Meta
                  className="text-center"
                  description={
                    <span style={{ color: red.primary }}>
                      <WarningOutlined style={{ marginRight: "16px" }} />
                      {item}
                    </span>
                  }
                />
              </List.Item>
            )}
          />
        )}
        <Input name="id" type="hidden" />

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="code"
              label={t("product.code")}
              rules={[
                {
                  required: true,
                  message: t("required.code"),
                },
              ]}
            >
              <Input disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="productTypeId"
              label={t("common.product_type")}
              rules={[
                {
                  required: true,
                  message: t("required.product_type"),
                },
              ]}
            >
              <Select
                style={{ width: "100%" }}
                placeholder=""
                disabled={type === EActionType.READ}
                options={productTypeList?.map((pt: IProductType) => ({
                  label: pt.name,
                  value: pt.id,
                }))}
              />
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
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
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="price"
              label={t("common.price")}
              rules={[
                {
                  required: true,
                  type: "number",
                  message: t("required.price"),
                },
              ]}
            >
              <InputNumber
                style={{ width: "100%" }}
                disabled={type === EActionType.READ}
                addonAfter={DEVISE}
              />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={12}>
            <Form.Item
              name="volumeId"
              label={t("common.volume")}
              rules={[
                {
                  required: true,
                  message: t("required.volume"),
                },
              ]}
            >
              <Select
                style={{ width: "100%" }}
                placeholder=""
                disabled={type === EActionType.READ}
                options={volumeList?.map((v: IVolume) => ({
                  label: v.quantity + " " + v.mesure,
                  value: v.id,
                }))}
              />
            </Form.Item>
          </Col>
          <Col className="gutter-row" span={12}>
            <Form.Item name="description" label={t("common.description")}>
              <Input.TextArea disabled={type === EActionType.READ} />
            </Form.Item>
          </Col>
        </Row>
        <Divider />
        <h3>{t("product.images")} </h3>
        <Row gutter={{ xs: 8, sm: 16, md: 24, lg: 32 }}>
          <Col className="gutter-row" span={24} style={{ textAlign: "center" }}>
            <Upload
              action=""
              accept=".jpeg, .png"
              maxCount={5}
              multiple
              listType="picture-card"
              fileList={fileList}
              disabled={type === EActionType.READ}
              onChange={onChangeImageHandler}
              onPreview={onPreviewImageHandler}
              beforeUpload={() => false}
            >
              {fileList.length >= 5 || type === EActionType.READ
                ? null
                : uploadButton}
            </Upload>
          </Col>
        </Row>
      </Form>
      <Modal
        open={previewOpen}
        footer={null}
        destroyOnClose
        onCancel={onCancelPreviewHandler}
      >
        <Carousel
          autoplay
          initialSlide={fileList.findIndex((file) => file.uid === previewFileUID)}
        >
          {fileList?.sort()?.map((file, index) => (
            <div key={index}>
              <div style={contentStyle}>
                <h3>{file?.name?.replace(/\.(jpe?g|png|gif|bmp)$/i, "")}</h3>
                <img
                  alt="example"
                  style={{ width: "100%", height: "100%" }}
                  src={getPreviewImage(file)}
                />
              </div>
            </div>
          ))}
        </Carousel>
      </Modal>
    </Modal>
  );
};

export default React.memo(ProductModal);
