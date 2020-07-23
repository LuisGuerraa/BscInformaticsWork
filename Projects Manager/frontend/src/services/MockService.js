import {API} from './Hateoas'

const apiHost = API.host
/**
 * Function used to obtain a mocked version of the service associated to the temperature resource
 */
export function getMockedProjectsService() {
    const projectsInfo = {
        count: 2,
        projects: [
            {
                name: "Grupo 3",
                description: "Grupo radiativo",
                issueStates: [
                    "open",
                    "closed"
                ],
                issueLabels: [
                    "defect",
                    "new_functionality"
                ]
            },
            {
                name: "Grupo 2",
                description: "Grupo dos desajeitados",
                issueStates: [
                    "open",
                    "archived"
                ],
                issueLabels: [
                    "exploration"
                ]
            }
        ]
    }
    
    return {
        getProjectsInfo: async () => {
            console.log(`MockedProjectsService.getProjectsInfo()`)
            return new Promise((resolve, reject) => {
                setTimeout(() => resolve(projectsInfo), 1000)
            })    
        },

        getProjectInfo: async (pname) => {
            console.log(`MockedProjectsService.getProjectInfo()`)
           
            return new Promise((resolve, reject) => {
                setTimeout(() => resolve(projectsInfo.projects.find((project) => project.name === pname) ), 1000)
            })  
        },

        insertProject: async (project) => {
            return new Promise((resolve, reject) => {
                setTimeout(() => resolve(projectsInfo.projects.push(project)), 1000)
            })    
        },
        //TODO promise not working !
        editProjectName:  (name, newName) => {
            console.log(`MockedProjectsService.editProjectName()`)
            projectsInfo.projects.find((project) => project['name'] === name).name = newName//['name'] // getting project name
           /* return new Promise((resolve, reject) => {
                setTimeout(() => resolve(
                    projectsInfo.projects.find(project => project['name'] === name)['name'] = newName), 1000)
            })  */
        },

        editProjectDescription:  (name, newDesc) => {
            console.log(`MockedProjectsService.editProjectDescription()`)
            projectsInfo.projects.find((project) => project['name'] === name).description = newDesc//['name'] // getting project name
           /* return new Promise((resolve, reject) => {
                setTimeout(() => resolve(
                    projectsInfo.projects.find(project => project['name'] === name)['name'] = newName), 1000)
            })  */
        },

        updateProject:  (project, oldName) => {
            console.log(`MockedProjectsService.updateProject()`)
            projectsInfo.projects.map(() => projectsInfo.projects.find((p) => p['name'] === oldName) || project)//['name'] // getting project name
           /* return new Promise((resolve, reject) => {
                setTimeout(() => resolve(
                    projectsInfo.projects.find(project => project['name'] === name)['name'] = newName), 1000)
            })  */
        },

        deleteProject:  (pname) => {
            console.log(`MockedProjectsService.deleteProject()`)
            projectsInfo.projects.splice(projectsInfo.projects.findIndex((p) => p['name'] === pname))//['name'] // getting project name
           /* return new Promise((resolve, reject) => {
                setTimeout(() => resolve(
                    projectsInfo.projects.find(project => project['name'] === name)['name'] = newName), 1000)
            })  */
        }
    }
}