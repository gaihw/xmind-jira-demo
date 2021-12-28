import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.atlassian.jira.rest.client.SearchRestClient;
import com.atlassian.jira.rest.client.domain.*;
import com.atlassian.jira.rest.client.domain.input.*;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import org.joda.time.DateTime;
import com.atlassian.jira.rest.client.IssueRestClient;
import com.atlassian.jira.rest.client.JiraRestClient;

import mjson.Json;
import org.xmind.core.*;
import org.xmind.core.io.ByteArrayStorage;

public class JiraUtil {

    private static boolean quiet = false;

    /**
     * 登录JIRA并返回指定的JiraRestClient对象
     *
     * @param username
     * @param password
     * @return
     * @throws URISyntaxException
     */
    public static JiraRestClient login_jira(String username, String password) {
        try {
            final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
            final URI jiraServerUri = new URI("http://localhost:7081/");
//            final URI jiraServerUri = new URI("https://jira.197749.com");

            final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, username,
                    password);
            return restClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取并返回指定的Issue对象
     *
     * @param issueNum
     * @param username
     * @param password
     * @return
     * @throws URISyntaxException
     */
    public static Issue get_issue(String issueNum, String username, String password) throws URISyntaxException {
        try {
            final JiraRestClient restClient = login_jira(username, password);
            final Promise<Issue> list = restClient.getIssueClient().getIssue(issueNum);
            Issue issue = list.get();
            return issue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA备注部分的内容
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static List<String> get_comments_body(Issue issue) throws URISyntaxException {
        try {
            List<String> comments = new ArrayList<String>();
            for (Comment comment : issue.getComments()) {
                comments.add(comment.getBody().toString());
            }
            return comments;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的创建时间
     *
     * @return
     * @throws URISyntaxException
     */
    public static DateTime get_create_time(Issue issue) throws URISyntaxException {
        try {
            return issue.getCreationDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的描述部分
     *
     * @return
     * @throws URISyntaxException
     */
    public static String get_description(Issue issue) throws URISyntaxException {
        try {
            return issue.getDescription();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的标题
     *
     * @return
     * @throws URISyntaxException
     */
    public static String get_summary(Issue issue) throws URISyntaxException {
        try {
            return issue.getSummary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的报告人的名字
     *
     * @return
     * @throws URISyntaxException
     */
    public static String get_reporter(Issue issue) throws URISyntaxException {
        try {
            return issue.getReporter().getDisplayName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的分派人的姓名列表
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static ArrayList<String> get_assignees(Issue issue) throws URISyntaxException {
        try {
            Json json = Json.read(issue.getFieldByName("分派给").getValue().toString());
            Iterator<Json> assignees = json.asJsonList().iterator();
            ArrayList<String> assigneesNames = new ArrayList<String>();
            while (assignees.hasNext()) {
                Json assignee = Json.read(assignees.next().toString());
                String assigneeName = assignee.at("displayName").toString();
                assigneesNames.add(assigneeName);
            }
            return assigneesNames;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的状态
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_status(Issue issue) throws URISyntaxException {
        try {
            return issue.getStatus().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的类型
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_issue_type(Issue issue) throws URISyntaxException {
        try {
            return issue.getIssueType().getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的模块
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static ArrayList<String> get_modules(Issue issue) throws URISyntaxException {
        try {
            ArrayList<String> arrayList = new ArrayList<String>();
            Iterator<BasicComponent> basicComponents = issue.getComponents().iterator();
            while (basicComponents.hasNext()) {
                String moduleName = basicComponents.next().getName();
                arrayList.add(moduleName);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的前端人员的姓名列表
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static ArrayList<String> get_qianduans(Issue issue) throws URISyntaxException {
        try {
            ArrayList<String> qianduanList = new ArrayList<String>();
            Json json = Json.read(issue.getFieldByName("前端").getValue().toString());
            Iterator<Json> qianduans = json.asJsonList().iterator();
            while (qianduans.hasNext()) {
                Json qianduan = Json.read(qianduans.next().toString());
                String qianduanName = qianduan.at("displayName").toString();
                qianduanList.add(qianduanName);
            }
            return qianduanList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的开发的姓名列表
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static ArrayList<String> get_developers(Issue issue) throws URISyntaxException {
        try {
            ArrayList<String> developersList = new ArrayList<String>();
            Json json = Json.read(issue.getFieldByName("开发").getValue().toString());
            Iterator<Json> developers = json.asJsonList().iterator();
            while (developers.hasNext()) {
                Json developer = Json.read(developers.next().toString());
                String developerName = developer.at("displayName").toString();
                developersList.add(developerName);
            }
            return developersList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的产品人员的姓名
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_product(Issue issue) throws URISyntaxException {
        try {
            String product_field = issue.getFieldByName("产品人员").getValue().toString();
            return Json.read(product_field).at("displayName").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的UE开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_UE_start_time(Issue issue) throws URISyntaxException {
        try {
            String UE_start_time = issue.getFieldByName("UE开始时间").getValue().toString();
            return UE_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的UE结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_UE_end_time(Issue issue) throws URISyntaxException {
        try {
            String UE_end_time = issue.getFieldByName("UE结束时间").getValue().toString();
            return UE_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的UI开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_UI_start_time(Issue issue) throws URISyntaxException {
        try {
            String UI_start_time = issue.getFieldByName("UI开始时间").getValue().toString();
            return UI_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的UI结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_UI_end_time(Issue issue) throws URISyntaxException {
        try {
            String UI_end_time = issue.getFieldByName("UI结束时间").getValue().toString();
            return UI_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的客户端开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_app_start_time(Issue issue) throws URISyntaxException {
        try {
            String app_start_time = issue.getFieldByName("客户端开始时间").getValue().toString();
            return app_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的客户端结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_app_end_time(Issue issue) throws URISyntaxException {
        try {
            String app_end_time = issue.getFieldByName("客户端结束时间").getValue().toString();
            return app_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的前端开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_qianduan_start_time(Issue issue) throws URISyntaxException {
        try {
            String qianduan_start_time = issue.getFieldByName("前端开始时间").getValue().toString();
            return qianduan_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的前端结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_qianduan_end_time(Issue issue) throws URISyntaxException {
        try {
            String qianduan_end_time = issue.getFieldByName("前端结束时间").getValue().toString();
            return qianduan_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的开发开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_develop_start_time(Issue issue) throws URISyntaxException {
        try {
            String develop_start_time = issue.getFieldByName("开发开始时间").getValue().toString();
            return develop_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的开发结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_develop_end_time(Issue issue) throws URISyntaxException {
        try {
            String develop_end_time = issue.getFieldByName("开发结束时间").getValue().toString();
            return develop_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的联调开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_liantiao_start_time(Issue issue) throws URISyntaxException {
        try {
            String liantiao_start_time = issue.getFieldByName("联调开始时间").getValue().toString();
            return liantiao_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的联调结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_liantiao_end_time(Issue issue) throws URISyntaxException {
        try {
            String liantiao_end_time = issue.getFieldByName("联调结束时间").getValue().toString();
            return liantiao_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的测试开始时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_test_start_time(Issue issue) throws URISyntaxException {
        try {
            String test_start_time = issue.getFieldByName("测试开始时间").getValue().toString();
            return test_start_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定JIRA的测试结束时间
     *
     * @param issue
     * @return
     * @throws URISyntaxException
     */
    public static String get_test_end_time(Issue issue) throws URISyntaxException {
        try {
            String test_end_time = issue.getFieldByName("测试结束时间").getValue().toString();
            return test_end_time;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Transition getTransitionByName(
            Iterable<Transition> transitions, String transitionName) {
        for (Transition transition : transitions) {
            if (transition.getName().equals(transitionName)) {
                return transition;
            }
        }
        return null;
    }

    /**
     * 获得全部项目信息
     *
     * @param restClient
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void getAllProjects(final JiraRestClient restClient)
            throws InterruptedException, ExecutionException {
        try {

            Promise<Iterable<BasicProject>> list = restClient
                    .getProjectClient().getAllProjects();
            Iterable<BasicProject> a = list.get();
            Iterator<BasicProject> it = a.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }

        } finally {
        }
    }

    /**
     * 获得单一项目信息
     *
     * @param restClient
     * @param porjectKEY
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void getProject(final JiraRestClient restClient,
                                   String porjectKEY) throws InterruptedException, ExecutionException {
        try {

            Project project = restClient.getProjectClient()
                    .getProject(porjectKEY).get();
            System.out.println(project);

        } finally {
        }
    }

    /**
     * 获得单一问题信息
     *
     * @param restClient
     * @param issueKEY
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void getIssue(final JiraRestClient restClient,
                                 String issueKEY) throws InterruptedException, ExecutionException {
        try {

            Promise<Issue> list = restClient.getIssueClient()
                    .getIssue(issueKEY);
            Issue issue = list.get();
            System.out.println(issue);

        } finally {
        }
    }

    /**
     * 建立问题
     *
     * @param jiraRestClient
     * @param newIssue
     * @return
     */
    public static BasicIssue createIssue(final JiraRestClient jiraRestClient,
                                         IssueInput newIssue) {
        BasicIssue basicIssue = jiraRestClient.getIssueClient()
                .createIssue(newIssue).claim();
        return basicIssue;
    }

    /**
     * 添加备注到问题
     *
     * @param jiraRestClient
     * @param issue
     * @param comment
     */
    public static void addCommentToIssue(final JiraRestClient jiraRestClient, Issue issue, String comment) {
        IssueRestClient issueClient = jiraRestClient.getIssueClient();
        issueClient.addComment(issue.getCommentsUri(), Comment.valueOf(comment)).claim();
    }

    /**
     * 经过标题获取问题
     *
     * @param jiraRestClient
     * @param label
     * @return
     */
    public static Iterable findIssuesByLabel(final JiraRestClient jiraRestClient, String label) {
        SearchRestClient searchClient = jiraRestClient.getSearchClient();
        String jql = "labels%3D" + label;
        com.atlassian.jira.rest.client.domain.SearchResult results = ((SearchRestClient) jiraRestClient).searchJql(jql).claim();
        return results.getIssues();
    }

    /**
     * 经过KEY获取问题
     *
     * @param jiraRestClient
     * @param issueKey
     * @return
     */
    public static Issue findIssueByIssueKey(final JiraRestClient jiraRestClient, String issueKey) {
        SearchRestClient searchClient = jiraRestClient.getSearchClient();
        String jql = "issuekey = \"" + issueKey + "\"";
        SearchResult results = searchClient.searchJql(jql).claim();
        return (Issue) results.getIssues().iterator().next();
    }

    /**
     * 建立问题 ：仅有简单问题名称
     *
     * @param restClient
     * @param porjectKEY
     * @param issueName
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void addIssue(final JiraRestClient restClient,
                                 String porjectKEY, String issueName) throws InterruptedException,
            ExecutionException {
        try {
//            10002L 任务
//            10001L 故事
//            10003L 子任务
//            10004L 故障
            IssueInputBuilder builder = new IssueInputBuilder(porjectKEY,
                    10101L, issueName);

            builder.setDescription("issue description");
            final IssueInput input = builder.build();

            try {
                // create issue
                final IssueRestClient client = restClient.getIssueClient();
                final BasicIssue issue = client.createIssue(input).claim();
                final Issue actual = client.getIssue(issue.getKey()).claim();
                System.out.println(actual);
            } finally {
                if (restClient != null) {
                    // restClient.close();
                }
            }

        } finally {
        }
    }

    /**
     * 建立问题 ：包含自定义字段
     *
     * @param restClient
     * @param porjectKEY
     * @param issueName
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void addIssueComplexAndXmind(JiraRestClient restClient,
                                                String porjectKEY, String issueName, IWorkbook workbook) throws InterruptedException,
            ExecutionException {


        IssueInputBuilder builder = null;

        ISheet defSheet = workbook.getPrimarySheet();// 获取主Sheet
        ITopic rootTopic = defSheet.getRootTopic(); // 获取根Topic
        String className = rootTopic.getTitleText();//节点TitleText
        List<ITopic> allChildren = rootTopic.getAllChildren();//获取所有子节点
        for (ITopic it : allChildren
        ) {
            for (ITopic iTopic : it.getAllChildren()
            ) {
                System.out.println("[" + className + "][" + it.getTitleText() + "]" + iTopic.getTitleText());
                issueName = "[" + className + "][" + it.getTitleText() + "]" + iTopic.getTitleText();
                builder = new IssueInputBuilder(porjectKEY,
                        10101L, issueName);
                String desc = "";
                for (ITopic iTopic1 : iTopic.getAllChildren()
                ) {
                    System.out.println(iTopic1.getTitleText() + ":");
                    desc = desc + iTopic1.getTitleText() + ":\r\n";
                    for (ITopic iTopic2 : iTopic1.getAllChildren()
                    ) {
                        System.out.println(iTopic2.getTitleText());
                        desc = desc + iTopic2.getTitleText() + "\r\n";

                    }
                }

                builder.setDescription(desc);
                builder.setPriorityId(3L);
                //单行文本
//            builder.setFieldValue("customfield_10042", "单行文本测试");

                //单选字段
//            builder.setFieldValue("customfield_10043", ComplexIssueInputFieldValue.with("value", "通常"));

                //数值自定义字段
//            builder.setFieldValue("customfield_10044", 100.08);

                builder.setAssigneeName("test");

                //用户选择自定义字段
//            builder.setFieldValue("customfield_10045", ComplexIssueInputFieldValue.with("name", "admin"));

                //用户选择自定义字段(多选)
//            Map<String, Object> user1 = new HashMap<String, Object>();
//            user1.put("name", "admin");
//            Map<String, Object> user2 = new HashMap<String, Object>();
//            user2.put("name", "wangxn");
//            ArrayList peoples = new ArrayList();
//            peoples.add(user1);
//            peoples.add(user2);
//            builder.setFieldValue("customfield_10047", peoples);

                //设定父问题
//            Map<String, Object> parent = new HashMap<String, Object>();
//            parent.put("key", "DEMO-1");
//            FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
//            builder.setFieldInput(parentField);

                final IssueInput input = builder.build();

                final IssueRestClient client = restClient.getIssueClient();
                final BasicIssue issue = client.createIssue(input).claim();
                LinkIssuesInput linkIssuesInput = new LinkIssuesInput(issue.getKey(), "DEMO-1", "Blocks");
                client.linkIssue(linkIssuesInput).claim();
                final Issue actual = client.getIssue(issue.getKey()).claim();
                System.out.println("创建问题：" + actual);
            }
        }
    }

    /**
     * 创建问题
     * @param restClient
     * @param porjectKEY
     * @param issueName
     */
    private static void addIssueComplex(JiraRestClient restClient,
                                        String porjectKEY, String issueName){


        IssueInputBuilder builder = null;
        builder = new IssueInputBuilder(porjectKEY,
                10101L, issueName);
        String desc = "添加问题测试...";
        builder.setDescription(desc);
        //设置优先级  1L P0；2L P1；3L P2；4L P3；5L P4
        builder.setPriorityId(3L);
        //单行文本
//            builder.setFieldValue("customfield_10042", "单行文本测试");

        //单选字段
//            builder.setFieldValue("customfield_10043", ComplexIssueInputFieldValue.with("value", "通常"));

        //数值自定义字段
//            builder.setFieldValue("customfield_10044", 100.08);

        builder.setAssigneeName("test");

        //用户选择自定义字段
//            builder.setFieldValue("customfield_10045", ComplexIssueInputFieldValue.with("name", "admin"));

        //用户选择自定义字段(多选)
//            Map<String, Object> user1 = new HashMap<String, Object>();
//            user1.put("name", "admin");
//            Map<String, Object> user2 = new HashMap<String, Object>();
//            user2.put("name", "wangxn");
//            ArrayList peoples = new ArrayList();
//            peoples.add(user1);
//            peoples.add(user2);
//            builder.setFieldValue("customfield_10047", peoples);

        //设定父问题
//            Map<String, Object> parent = new HashMap<String, Object>();
//            parent.put("key", "DEMO-1");
//            FieldInput parentField = new FieldInput("parent", new ComplexIssueInputFieldValue(parent));
//            builder.setFieldInput(parentField);

        final IssueInput input = builder.build();

        final IssueRestClient client = restClient.getIssueClient();
        final BasicIssue issue = client.createIssue(input).claim();
        LinkIssuesInput linkIssuesInput = new LinkIssuesInput(issue.getKey(), "DEMO-1", "Blocks");
        client.linkIssue(linkIssuesInput).claim();
        final Issue actual = client.getIssue(issue.getKey()).claim();
        System.out.println("创建问题：" + actual);
    }

    /**
     * 获取问题的全部字段
     *
     * @param restClient
     * @param issueKEY
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void getIssueFields(final JiraRestClient restClient,
                                       String issueKEY) throws InterruptedException, ExecutionException {
        try {

            Promise<Issue> list = restClient.getIssueClient()
                    .getIssue(issueKEY);
            Issue issue = list.get();
            Iterable<Field> fields = issue.getFields();
            Iterator<Field> it = fields.iterator();
            while (it.hasNext()) {
                System.out.println(it.next());
            }

        } finally {
        }
    }

    /**
     * 获取问题类型值
     * Type ID = 10002, Name = 任务
     * Type ID = 10003, Name = 子任务
     * Type ID = 10001, Name = 故事
     * Type ID = 10004, Name = 故障
     * Type ID = 10000, Name = Epic
     * Type ID = 10101, Name = Test Case
     * Type ID = 10102, Name = Test Case1
     *
     * @param client
     * @throws Exception
     */
    private static void listAllIssueTypes(JiraRestClient client) throws Exception {
        Promise<Iterable<IssueType>> promise = client.getMetadataClient().getIssueTypes();
        Iterable<IssueType> issueTypes = promise.claim();
        for (IssueType it : issueTypes) {
            System.out.println("Type ID = " + it.getId() + ", Name = " + it.getName());
        }
    }

    /**
     * 测试函数
     *
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws Exception {
//        String username = "gaihongwei";
//        String password = "Gaihw0324";
        String username = "test";
        String password = "123456";
        String issueName = "用例1";
        JiraRestClient restClient = login_jira(username, password);
//        System.out.println(restClient.getUserClient().getUser(username).claim().getEmailAddress());
//        getAllProjects(restClient);
//        getIssue(restClient,"DEMO-1");
//        getIssueFields(restClient,issueNum);
//        findIssueByIssueKey(restClient,issueNum);
//        listAllIssueTypes(restClient);
//        System.out.println(get_issue_type(get_issue(issueNum,username,password)));
//        addIssue(restClient,"DEMO","demo");
//        createIssue(restClient,issueNum);
        //创建问题
//        addIssueComplex(restClient,"DEMO",issueName);
//        final Issue issue = get_issue(issueNum, username, password);
//        JiraInfoModel jiraInfoModel = get_jira_info(issue);
//        System.out.println(jiraInfoModel.getSummary());

        String path = "/Users/mac/Documents/8.xmind";
        IWorkbookBuilder builder = Core.getWorkbookBuilder();// 初始化builder
        IWorkbook workbook = null;
        try {
            // 打开XMind文件
            workbook = builder.loadFromFile(new File(path), new ByteArrayStorage(), null);
//            workbook = builder.loadFromPath(path,new ByteArrayStorage(), null);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        addIssueComplexAndXmind(restClient, "DEMO", issueName, workbook);

    }

}