import pickle

# f = open('dict_word.pkl', 'rb')
# for line in f:
#     print(line)
items = pickle.load(open("/Users/wangsiwei/Downloads/wdm-project-benchmark-master/consistency-test/tmp/item_ids.pkl", 'rb'), encoding='utf-8')
user_ids = pickle.load(open("/Users/wangsiwei/Downloads/wdm-project-benchmark-master/consistency-test/tmp/user_ids.pkl", 'rb'),encoding='iso-8859-1')
print(len(items))
print(len(user_ids))