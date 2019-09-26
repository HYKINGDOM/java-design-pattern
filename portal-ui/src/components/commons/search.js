import axios from "axios";
export default function (url, data, status, callback) {
    status.loading = true;
    axios
        .get(url, { params: data })
        .then(response => {
            let page = response.data;
            page.number = this.page.number + 1;
            status.loading = false;

            callback(page);
        })
        .catch(error => (status.loading = false));
}