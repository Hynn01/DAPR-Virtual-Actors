import csv


def read_from_tsv(file_path: str, column_names: list) -> list:
    csv.register_dialect('tsv_dialect', delimiter='\t', quoting=csv.QUOTE_ALL)
    with open(file_path, "r") as wf:
        reader = csv.DictReader(wf, fieldnames=column_names, dialect='tsv_dialect')
        datas = []
        for row in reader:
            data = dict(row)
            datas.append(data)
    csv.unregister_dialect('tsv_dialect')
    return datas


hh = read_from_tsv("/Users/wangsiwei/Downloads/wdm-project-benchmark-master/hh.py",
                   ["INPUT:original", "INPUT:annotation", "GOLDEN:result", "GOLDEN:entity1","GOLDEN:entity2", "GOLDEN:entity3", "GOLDEN:entity4", "GOLDEN:entity5", "GOLDEN:entity6","GOLDEN:entity7","GOLDEN:entity8", "GOLDEN:attribute1", "GOLDEN:attribute2", "GOLDEN:attribute3", "GOLDEN:attribute4","GOLDEN:attribute5", "GOLDEN:attribute6", "GOLDEN:attribute7", "GOLDEN:attribute8", "HINT:text","TASK:id","TASK:overlap", "TASK:remaining_overlap"])
print(hh)
