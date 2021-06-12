import pickle

# f = open('dict_word.pkl', 'rb')
# for line in f:
#     print(line)
items = pickle.load(open("./tmp/item_ids.pkl", 'rb'), encoding='utf-8')
user_ids = pickle.load(open("./tmp/user_ids.pkl", 'rb'),encoding='iso-8859-1')
print(items)
print(len(items))
print(user_ids)
print(len(user_ids))