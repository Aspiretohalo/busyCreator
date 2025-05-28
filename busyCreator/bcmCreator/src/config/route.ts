import Index from "../pages/Index.vue";
import Personal from "../pages/personal/Personal.vue";
import Create from "../pages/create/Create.vue";
import Find from "../pages/find/Find.vue";
import Project from "../pages/projects/Project.vue";
import HistorySession from "../pages/create/HistorySession.vue";
import Chat from "../pages/create/Chat.vue";
import Profile from "../pages/personal/Profile.vue";
import Team from "../pages/team/Team.vue";
import Projects from "../pages/personal/Projects.vue";
import Portfolio from "../pages/personal/Portfolio.vue";
import WorksDetail from "../pages/projects/detail/WorksDetail.vue";
import TeamDetail from "../pages/team/TeamDetail.vue";
import TeamProject from "../pages/team/manage/TeamProject.vue";
import TeamPermission from "../pages/team/manage/TeamPermission.vue";
import TeamSetting from "../pages/team/manage/TeamSetting.vue";
import ProjectDetail from "../pages/projects/detail/ProjectDetail.vue";
import LoginSingle from "../pages/LoginSingle.vue";
import RegisterSingle from "../pages/RegisterSingle.vue";

const routes = [
    {
        path: "/", component: Index,
        children: [
            {
                path: "", component: Find,
            },
            {
                path: "create", component: Create
            },
            {
                path: "project", component: Project
            },
            {
                path: "team", component: Team
            },
            {
                path: "/personal", component: Personal,
                children: [
                    { path: "profile", name: 'profile', component: Profile },
                    { path: "team", name: 'team', component: Team },
                    { path: "projects", name: 'projects', component: Projects },
                    { path: "portfolio", name: 'portfolio', component: Portfolio },
                ]
            },
            {
                path: "historySession", component: HistorySession
            },
            {
                path: '/chat/:sessionId',
                name: 'chat',
                component: Chat
            },
        ]
    },
    {
        path: "/works/:id", name: 'worksDetail', component: WorksDetail,
    },
    {
        path: "/project/:id", name: 'projectDetail', component: ProjectDetail,
    },

    {
        path: "/team/:id", name: 'teamDetail', component: TeamDetail,
        children: [
            { path: "project", name: 'teamProject', component: TeamProject },
            { path: "permission", name: 'teamPermission', component: TeamPermission },
            { path: "setting", name: 'teamSetting', component: TeamSetting },
        ]
    },
    { path: "/login", component: LoginSingle },
    { path: "/register", component: RegisterSingle },
];

export default routes;
