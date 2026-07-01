import csv
import random
import os
from datetime import datetime, timedelta

# Constants
PRODUCT_NAMES = [
    'Milk', 'Bread', 'Coffee', 'Tea', 'Rice', 'Medicine', 'Book',
    'Juice', 'Cake', 'Candy', 'Water', 'Chocolate', 'Noodles',
    'Soap', 'Shampoo', 'Snack', 'Egg', 'Cheese', 'Butter', 'Sugar'
]

START_DATE = datetime(2026, 1, 1)
END_DATE = datetime(2027, 12, 31)
TODAY = datetime.now().date()

PRICE_OPTIONS = [10000, 15000, 20000, 25000, 30000, 35000, 40000, 
                 45000, 50000, 55000, 60000, 65000, 70000, 75000, 
                 80000, 85000, 90000, 95000, 100000]

PRODUCT_BARCODES = {
    name: f"P{i + 1:04d}"
    for i, name in enumerate(PRODUCT_NAMES)
}

INITIAL_BATCH_START = 1
ADD_BATCH_START = 100001

def create_directories():
    """Create required folder structure"""
    dirs = ['data/initial', 'data/transactions', 'data/output']
    for dir_path in dirs:
        os.makedirs(dir_path, exist_ok=True)

def generate_random_date():
    """Generate a random date between START_DATE and END_DATE"""
    delta = END_DATE - START_DATE
    random_days = random.randint(0, delta.days)
    return START_DATE + timedelta(days=random_days)

def generate_near_expiry_date():
    """Generate a date within 7 days from today"""
    days_offset = random.randint(1, 7)
    return TODAY + timedelta(days=days_offset)

def generate_warehouse_dataset(filename, num_records):
    """Generate initial warehouse dataset with multiple batches per product"""
    
    # Calculate number of near-expiry batches (10-15%)
    near_expiry_count = int(num_records * random.uniform(0.10, 0.15))
    near_expiry_indices = set(random.sample(range(num_records), near_expiry_count))
    
    # Use a stable barcode for each product name across every generated file.
    # Initial datasets can only contain products from the fixed product list.
    num_unique_products = max(1, min(
        len(PRODUCT_NAMES),
        int(num_records * random.uniform(0.20, 0.50))
    ))

    selected_products = random.sample(PRODUCT_NAMES, num_unique_products)
    
    # Distribute records among products with at least 1 batch per product
    batch_distribution = [1] * num_unique_products  # Start with 1 batch each
    remaining_batches = num_records - num_unique_products
    
    # Randomly distribute remaining batches
    for _ in range(remaining_batches):
        product_idx = random.randint(0, num_unique_products - 1)
        batch_distribution[product_idx] += 1
    
    # Shuffle to randomize product order
    product_order = list(range(num_unique_products))
    random.shuffle(product_order)
    
    records = []
    batch_counter = INITIAL_BATCH_START
    
    record_idx = 0
    for product_idx in product_order:
        product_name = selected_products[product_idx]
        barcode = PRODUCT_BARCODES[product_name]
        num_batches = batch_distribution[product_idx]
        
        for _ in range(num_batches):
            # Generate batch code
            batch_code = f"B{batch_counter:05d}"
            batch_counter += 1
            
            # Generate expiry date
            if record_idx in near_expiry_indices:
                expiry_date = generate_near_expiry_date()
            else:
                expiry_date = generate_random_date()
                # Ensure not within 7 days if not near expiry
                while (expiry_date.date() - TODAY).days <= 7:
                    expiry_date = generate_random_date()
            
            quantity = random.randint(1, 100)
            price = random.choice(PRICE_OPTIONS)
            
            records.append({
                'barcode': barcode,
                'productName': product_name,
                'batchCode': batch_code,
                'expiryDate': expiry_date.strftime('%Y-%m-%d'),
                'quantity': quantity,
                'price': price
            })
            record_idx += 1
    
    # Write to CSV
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['barcode', 'productName', 'batchCode', 'expiryDate', 'quantity', 'price']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(records)
    
    # Statistics
    unique_products = len(set(r['barcode'] for r in records))
    unique_batches = len(set(r['batchCode'] for r in records))
    near_expiry = sum(1 for r in records if (datetime.strptime(r['expiryDate'], '%Y-%m-%d').date() - TODAY).days <= 7)
    sorted_barcodes = sorted(set(r['barcode'] for r in records))
    barcode_range = f"{sorted_barcodes[0]} - {sorted_barcodes[-1]}"
    batch_range = f"B{INITIAL_BATCH_START:05d} - B{batch_counter - 1:05d}"
    
    return {
        'rows': len(records),
        'unique_products': unique_products,
        'unique_batches': unique_batches,
        'near_expiry': near_expiry,
        'barcode_range': barcode_range,
        'batch_range': batch_range
    }

def generate_add_transactions(filename, warehouse_file, num_transactions):
    """Generate ADD transactions using existing products from warehouse"""
    
    # Read warehouse data to get existing products
    warehouse_records = []
    with open(warehouse_file, 'r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            warehouse_records.append(row)
    
    # Get unique products (barcode, productName pairs)
    unique_products = {}
    for record in warehouse_records:
        barcode = record['barcode']
        product_name = record['productName']
        unique_products[barcode] = product_name
    
    product_list = list(unique_products.items())
    
    max_batch_num = max(int(r['batchCode'][1:]) for r in warehouse_records)
    next_batch_num = max(ADD_BATCH_START, max_batch_num + 1)
    
    records = []
    for i in range(num_transactions):
        # Select random product
        barcode, product_name = random.choice(product_list)
        
        # Generate new batch code
        batch_code = f"B{next_batch_num:05d}"
        next_batch_num += 1
        
        # Generate expiry date (some near expiry)
        if random.random() < 0.15:  # 15% near expiry
            expiry_date = generate_near_expiry_date()
        else:
            expiry_date = generate_random_date()
            while (expiry_date.date() - TODAY).days <= 7:
                expiry_date = generate_random_date()
        
        quantity = random.randint(1, 100)
        price = random.choice(PRICE_OPTIONS)
        
        records.append({
            'action': 'ADD',
            'barcode': barcode,
            'productName': product_name,
            'batchCode': batch_code,
            'expiryDate': expiry_date.strftime('%Y-%m-%d'),
            'quantity': quantity,
            'price': price
        })
    
    # Write to CSV
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['action', 'barcode', 'productName', 'batchCode', 'expiryDate', 'quantity', 'price']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(records)
    
    # Statistics
    unique_products_used = len(set(r['barcode'] for r in records))
    near_expiry = sum(1 for r in records if (datetime.strptime(r['expiryDate'], '%Y-%m-%d').date() - TODAY).days <= 7)
    
    return {
        'rows': len(records),
        'unique_products': unique_products_used,
        'near_expiry': near_expiry,
        'batch_range': f"B{max(ADD_BATCH_START, max_batch_num + 1):05d} - B{next_batch_num - 1:05d}"
    }

def generate_dispatch_transactions(filename, warehouse_file, num_transactions):
    """Generate DISPATCH transactions using existing products from warehouse"""
    
    # Read warehouse data to get existing products
    warehouse_records = []
    with open(warehouse_file, 'r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            warehouse_records.append(row)
    
    # Get unique products
    unique_products = set(r['barcode'] for r in warehouse_records)
    product_list = list(unique_products)
    
    records = []
    for _ in range(num_transactions):
        barcode = random.choice(product_list)
        quantity = random.randint(1, 50)
        
        records.append({
            'action': 'DISPATCH',
            'barcode': barcode,
            'quantity': quantity
        })
    
    # Write to CSV
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['action', 'barcode', 'quantity']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(records)
    
    # Statistics
    unique_products_used = len(set(r['barcode'] for r in records))
    
    return {
        'rows': len(records),
        'unique_products': unique_products_used
    }

def main():
    print("=" * 80)
    print("WAREHOUSE MANAGEMENT SYSTEM - DATASET GENERATOR")
    print("For CSD201 Project: AVL Tree + FIFO Queue")
    print("=" * 80)
    print()
    
    # Create directory structure
    create_directories()
    print("📁 Directory structure created:")
    print("   data/initial/")
    print("   data/transactions/")
    print("   data/output/")
    print()
    
    print("=" * 80)
    print("INITIAL WAREHOUSE DATASETS")
    print("=" * 80)
    print()
    
    # Generate initial warehouse datasets
    warehouse_sizes = [100, 500, 1000, 5000, 10000]
    warehouse_stats = {}
    
    for size in warehouse_sizes:
        filename = f"data/initial/warehouse_{size}.csv"
        print(f"📊 Generating {filename}...")
        stats = generate_warehouse_dataset(filename, size)
        warehouse_stats[f"warehouse_{size}"] = stats
        print(f"   ✓ {stats['rows']:,} rows")
        print(f"   ✓ {stats['unique_products']} unique products")
        print(f"   ✓ {stats['unique_batches']} unique batches")
        print(f"   ✓ {stats['near_expiry']} near-expiry batches ({stats['near_expiry']/stats['rows']*100:.1f}%)")
        print(f"   ✓ Barcode range: {stats['barcode_range']}")
        print(f"   ✓ Batch range: {stats['batch_range']}")
        print()
    
    print("=" * 80)
    print("ADD TRANSACTION FILES")
    print("=" * 80)
    print()
    
    # Generate ADD transaction files
    add_sizes = [100, 500, 1000]
    add_stats = {}
    
    for size in add_sizes:
        warehouse_file = f"data/initial/warehouse_1000.csv"  # Use 1000-record warehouse as base
        filename = f"data/transactions/add_{size}.csv"
        print(f"📊 Generating {filename}...")
        stats = generate_add_transactions(filename, warehouse_file, size)
        add_stats[f"add_{size}"] = stats
        print(f"   ✓ {stats['rows']:,} ADD transactions")
        print(f"   ✓ {stats['unique_products']} unique products used")
        print(f"   ✓ {stats['near_expiry']} near-expiry batches")
        print(f"   ✓ Batch range: {stats['batch_range']}")
        print()
    
    print("=" * 80)
    print("DISPATCH TRANSACTION FILES")
    print("=" * 80)
    print()
    
    # Generate DISPATCH transaction files
    dispatch_sizes = [100, 500, 1000]
    dispatch_stats = {}
    
    for size in dispatch_sizes:
        warehouse_file = f"data/initial/warehouse_1000.csv"  # Use 1000-record warehouse as base
        filename = f"data/transactions/dispatch_{size}.csv"
        print(f"📊 Generating {filename}...")
        stats = generate_dispatch_transactions(filename, warehouse_file, size)
        dispatch_stats[f"dispatch_{size}"] = stats
        print(f"   ✓ {stats['rows']:,} DISPATCH transactions")
        print(f"   ✓ {stats['unique_products']} unique products used")
        print()
    
    print("=" * 80)
    print("✅ All datasets generated successfully!")
    print("=" * 80)
    print()
    print("📁 Files created:")
    print("   data/initial/")
    for size in warehouse_sizes:
        print(f"      ✓ warehouse_{size}.csv")
    print("   data/transactions/")
    for size in add_sizes:
        print(f"      ✓ add_{size}.csv")
    for size in dispatch_sizes:
        print(f"      ✓ dispatch_{size}.csv")
    print("   data/output/")
    print("      ⚠️  Empty - ready for program output")
    print()
    print("=" * 80)
    print("📋 Summary Statistics")
    print("=" * 80)
    print()
    
    # Summary table
    print("INITIAL WAREHOUSE FILES:")
    print(f"{'File':<20} {'Rows':<10} {'Unique Products':<20} {'Batches':<15} {'Near-Expiry':<15}")
    print("-" * 80)
    for size in warehouse_sizes:
        key = f"warehouse_{size}"
        stats = warehouse_stats[key]
        print(f"{key:<20} {stats['rows']:<10} {stats['unique_products']:<20} {stats['unique_batches']:<15} {stats['near_expiry']:<15}")
    print()
    
    print("ADD TRANSACTION FILES:")
    print(f"{'File':<20} {'Rows':<10} {'Unique Products':<20} {'Near-Expiry':<15}")
    print("-" * 70)
    for size in add_sizes:
        key = f"add_{size}"
        stats = add_stats[key]
        print(f"{key:<20} {stats['rows']:<10} {stats['unique_products']:<20} {stats['near_expiry']:<15}")
    print()
    
    print("DISPATCH TRANSACTION FILES:")
    print(f"{'File':<20} {'Rows':<10} {'Unique Products':<20}")
    print("-" * 55)
    for size in dispatch_sizes:
        key = f"dispatch_{size}"
        stats = dispatch_stats[key]
        print(f"{key:<20} {stats['rows']:<10} {stats['unique_products']:<20}")
    print()
    
    print("=" * 80)
    print("🎯 Ready for Java CSD201 Project")
    print("   - Use AVL Tree for product lookup by barcode")
    print("   - Use FIFO Queue for inventory management")
    print("   - Process ADD and DISPATCH transactions")
    print("=" * 80)

if __name__ == "__main__":
    main()