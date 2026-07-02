import csv
import random
import os
from datetime import datetime, timedelta

# ==============================
# CONFIG
# ==============================

START_DATE = datetime(2026,1,1)
END_DATE = datetime(2027,12,31)

PRICE_OPTIONS = [
    10000,15000,20000,25000,
    30000,35000,40000,45000,
    50000,55000,60000,65000,
    70000,75000,80000,85000,
    90000,95000,100000
]

DATASET_SIZES = [100,500,1000,5000,10000]

# ==============================
# CREATE FOLDER
# ==============================

os.makedirs("data/benchmark", exist_ok=True)

# ==============================
# RANDOM DATE
# ==============================

def random_date():

    delta = END_DATE - START_DATE
    random_days = random.randint(0, delta.days)

    date = START_DATE + timedelta(days=random_days)

    return date.strftime("%Y-%m-%d")


# ==============================
# GENERATE BENCHMARK FILE
# ==============================

def generate_benchmark(filename, size):

    records=[]

    for i in range(size):

        barcode=f"P{i+1:05d}"

        product_name=f"Product_{i+1}"

        batch_code=f"B{i+1:05d}"

        expiry=random_date()

        quantity=random.randint(1,100)

        price=random.choice(PRICE_OPTIONS)

        records.append({

            "barcode":barcode,
            "productName":product_name,
            "batchCode":batch_code,
            "expiryDate":expiry,
            "quantity":quantity,
            "price":price

        })


    with open(
        filename,
        "w",
        newline="",
        encoding="utf-8"
    ) as csvfile:

        fieldnames=[

            "barcode",
            "productName",
            "batchCode",
            "expiryDate",
            "quantity",
            "price"

        ]

        writer=csv.DictWriter(
            csvfile,
            fieldnames=fieldnames
        )

        writer.writeheader()

        writer.writerows(records)


# ==============================
# MAIN
# ==============================

print("="*60)
print("Generating benchmark datasets...")
print("="*60)

for size in DATASET_SIZES:

    file_name=f"data/benchmark/benchmark_{size}.csv"

    generate_benchmark(
        file_name,
        size
    )

    print(f"✓ {file_name}")
    print(f"  Records: {size:,}")

print()
print("="*60)
print("DONE")
print("="*60)

print()
print("Created files:")
for size in DATASET_SIZES:
    print(f"benchmark_{size}.csv")