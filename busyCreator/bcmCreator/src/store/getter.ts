export default {
    selectCourses(state: any) {
        if (state.activeName == 'Coming')
            return state.courses.filter((course: any) => course.status === '即将开始');
        else if (state.activeName == 'Ongoing')
            return state.courses.filter((course: any) => course.status === '进行中');
        else if (state.activeName == 'Completed')
            return state.courses.filter((course: any) => course.status === '已结束');
        else
            return state.courses
    },
}