rs.initiate({
    _id: "rs0",
    members: [
        { _id: 0, host: "lil_mailbox_mongo1:27017" },
        { _id: 1, host: "lil_mailbox_mongo2:27017" },
        { _id: 2, host: "lil_mailbox_mongo3:27017" }
    ]
});
