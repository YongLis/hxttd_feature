import {Card, Form, Input, message, Select, Spin} from 'antd';
import {history, useLocation} from '@umijs/max';
import React from 'react';
import {getMetaFieldById, MetaFieldDetail} from '@/services/srv/metaFieldApi';
import {ObjectReturnTypeEnum, ObjectReturnTypeLabel} from '@/components/consts/AppEnum';
import GroovyEditor from '@/components/common/GroovyEditor';
import {getDictCodeOptions} from '@/services/srv/dictCodeApi';
import {CustomPageContainer} from '@/components';

const FieldDetail: React.FC = () => {
  const [form] = Form.useForm();
  const location = useLocation() as { state?: { id: number } };
  console.log(location);
  const id = location.state?.id;
  const [loading, setLoading] = React.useState(true);
  const [detail, setDetail] = React.useState<MetaFieldDetail | null>(null);
  const [categoryOptions, setCategoryOptions] = React.useState<{ label: string; value: string }[]>([]);

  // 加载分类标签字典
  React.useEffect(() => {
    getDictCodeOptions('ttd', 'meta_tag').then((opts) => {
      setCategoryOptions(opts);
    });
  }, []);

  const OBJECT_RETURN_TYPE = Object.values(ObjectReturnTypeEnum).map((code) => ({
    value: code,
    label: ObjectReturnTypeLabel[code],
  }));

  React.useEffect(() => {
    if (undefined === id) {
      message.error('缺少元字段ID');
      history.push('/meta-field/index');
      return;
    }
    setLoading(true);
    getMetaFieldById(Number(id))
        .then((res) => {
          if (res.code === '0000' && res.data) {
            setDetail(res.data);
            form.setFieldsValue(res.data);
          } else {
            message.error(res.message || '查询失败');
            history.push('/meta-field/index');
          }
        })
        .catch(() => {
          message.error('查询元字段详情失败');
          history.push('/meta-field/index');
        })
        .finally(() => {
          setLoading(false);
        });
  }, [id]);

  const handleBack = () => {
    history.push('/meta-field/index');
  };

  return (
      <CustomPageContainer title={false}>
        <Card>
          <Spin spinning={loading}>
            {detail ? (
                <Form form={form} layout="horizontal" style={{maxWidth: 720}} disabled>
                  <Form.Item name="resourceKey" label="元字段编码">
                    <Input/>
                  </Form.Item>

                  <Form.Item name="resourceName" label="元字段名称">
                    <Input/>
                  </Form.Item>

                  <Form.Item name="version" label="版本号">
                    <Input disabled/>
                  </Form.Item>

                  <Form.Item name="returnType" label="返回值类型">
                    <Select options={OBJECT_RETURN_TYPE}/>
                  </Form.Item>

                  <Form.Item name="defaultValue" label="默认值">
                    <Input placeholder="-"/>
                  </Form.Item>

                  <Form.Item name="exceptionValue" label="异常值">
                    <Input placeholder="-"/>
                  </Form.Item>

                  <Form.Item name="categoryTag" label="分类标签">
                    <Select options={categoryOptions}/>
                  </Form.Item>

                  <Form.Item name="language" label="脚本语言">
                    <Select>
                      <Select.Option value="groovy">Groovy</Select.Option>
                    </Select>
                  </Form.Item>

                  <Form.Item name="script" label="脚本">
                    <GroovyEditor val={detail.script} editable={false} onChange={() => {
                    }}/>
                  </Form.Item>
                </Form>
            ) : (
                !loading && <div style={{textAlign: 'center', padding: 40, color: '#999'}}>暂无数据</div>
            )}
          </Spin>
        </Card>
      </CustomPageContainer>
  );
};

export default FieldDetail;
