import axios from "axios";
function loadData(store, retry = false) {
    axios.get("/api/user-center/user").then(response => {
        let user = response.data;
        if (user.name) {
            store.commit("updateAccount", user);
        } else if (retry === false) {
            loadData(store, true);
        }
    });
}
export default loadData;