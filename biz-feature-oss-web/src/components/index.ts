/**
 * 这个文件作为组件的目录
 * 目的是统一管理对外输出的组件，方便分类
 */
/**
 * 布局组件
 */
import Footer from './Footer';
import {Question, SelectLang} from './RightContent';
import {AvatarDropdown, AvatarName} from './RightContent/AvatarDropdown';
import CustomPageContainer from './PageContainer';
import CustomProTable from './common/CustomProTable';

export {default as LineageGraph} from './LineageGraph';

export {AvatarDropdown, AvatarName, Footer, Question, SelectLang, CustomPageContainer, CustomProTable};
